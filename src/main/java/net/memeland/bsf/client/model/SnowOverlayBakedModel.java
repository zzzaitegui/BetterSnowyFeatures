package net.memeland.bsf.client.model;

import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.List;

public class SnowOverlayBakedModel implements BakedModel {

    public static final ModelProperty<Boolean> HAS_SNOW_ABOVE = new ModelProperty<>();

    private static final Long2ObjectMap<CachedResult> POSITION_CACHE = new Long2ObjectOpenHashMap<>();
    private static final Object CACHE_LOCK = new Object();
    private static final int MAX_CACHE_SIZE = 2048;
    private static final long CACHE_LIFETIME_MS = 2000;

    private static Minecraft cachedMinecraft = null;

    private final BakedModel baseModel;
    private final BakedModel snowyModel;

    private static class CachedResult {
        final boolean hasSnow;
        final long timestamp;

        CachedResult(boolean hasSnow, long timestamp) {
            this.hasSnow = hasSnow;
            this.timestamp = timestamp;
        }

        boolean isExpired(long currentTime) {
            return (currentTime - timestamp) > CACHE_LIFETIME_MS;
        }
    }

    public SnowOverlayBakedModel(BakedModel baseModel, BakedModel snowyModel) {
        this.baseModel = baseModel;
        this.snowyModel = snowyModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return baseModel.getQuads(state, direction, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random,
                                    ModelData extraData, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        Boolean hasSnowAbove = extraData.get(HAS_SNOW_ABOVE);

        if (hasSnowAbove != null && hasSnowAbove && direction != null && direction.getAxis().isHorizontal()) {
            return snowyModel.getQuads(state, direction, random, extraData, renderType);
        }

        return baseModel.getQuads(state, direction, random, extraData, renderType);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        if (!net.memeland.bsf.ModConfig.ENABLE_SNOW_OVERLAY.get()) {
            return modelData.derive().with(HAS_SNOW_ABOVE, false).build();
        }

        if (pos != null && pos.getY() < 55) {
            return modelData.derive().with(HAS_SNOW_ABOVE, false).build();
        }

        long currentTime = System.currentTimeMillis();
        long posKey = packPos(pos);

        synchronized (CACHE_LOCK) {
            CachedResult cached = POSITION_CACHE.get(posKey);
            if (cached != null && !cached.isExpired(currentTime)) {
                return modelData.derive().with(HAS_SNOW_ABOVE, cached.hasSnow).build();
            }
        }

        boolean hasSnow = false;
        try {
            if (cachedMinecraft == null) {
                cachedMinecraft = Minecraft.getInstance();
            }

            if (cachedMinecraft.level != null && pos != null && level != null) {
                boolean isCold = BiomeTemperatureHelper.isColdAt(cachedMinecraft.level, pos);

                if (isCold) {
                    BlockState aboveState = level.getBlockState(pos.above());
                    hasSnow = aboveState.is(BlockTags.SNOW);
                }
            }
        } catch (Exception e) {

        }

        synchronized (CACHE_LOCK) {
            if (POSITION_CACHE.size() >= MAX_CACHE_SIZE) {
                POSITION_CACHE.clear();
            }
            POSITION_CACHE.put(posKey, new CachedResult(hasSnow, currentTime));
        }

        return modelData.derive().with(HAS_SNOW_ABOVE, hasSnow).build();
    }

    private static long packPos(BlockPos pos) {
        if (pos == null) return 0;
        long x = pos.getX() & 0xFFFFF;
        long y = pos.getY() & 0xFFF;
        long z = pos.getZ() & 0xFFFFF;
        return (x << 32) | (y << 20) | z;
    }

    public static void clearCache() {
        synchronized (CACHE_LOCK) {
            POSITION_CACHE.clear();
        }
        cachedMinecraft = null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return baseModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return baseModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return baseModel.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return baseModel.getOverrides();
    }

    public net.minecraftforge.client.ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return net.minecraftforge.client.ChunkRenderTypeSet.of(
                net.minecraft.client.renderer.RenderType.solid()
        );
    }
}