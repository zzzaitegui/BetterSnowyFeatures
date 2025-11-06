package net.memeland.bsf.client.model;

import net.memeland.bsf.ModConfigs;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SnowyPlantBakedModel implements BakedModel {

    public static final ModelProperty<Integer> VARIANT_INDEX = new ModelProperty<>();

    private static final int REQUIRED_SNOW_NEIGHBORS = 2;
    private static final int NUM_VARIANTS = 11;

    private final BakedModel normalModel;
    private final BakedModel snowyTextureModel;
    private final Map<Integer, BakedModel> snowVariantModels;

    public SnowyPlantBakedModel(BakedModel normalModel, BakedModel snowyTextureModel, Map<Integer, BakedModel> snowVariantModels) {
        this.normalModel = normalModel;
        this.snowyTextureModel = snowyTextureModel;
        this.snowVariantModels = snowVariantModels;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return normalModel.getQuads(state, direction, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random,
                                    ModelData extraData, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        Integer variantIndex = extraData.get(VARIANT_INDEX);

        if (variantIndex != null && variantIndex >= 0 && snowVariantModels.containsKey(variantIndex)) {
            return snowVariantModels.get(variantIndex).getQuads(state, direction, random, extraData, renderType);
        }

        if (variantIndex != null && variantIndex == -1) {
            return snowyTextureModel.getQuads(state, direction, random, extraData, renderType);
        }

        return normalModel.getQuads(state, direction, random, extraData, renderType);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        int variantIndex = -2;


        if (!ModConfigs.ENABLE_PLANT_SNOW_LAYERS.get()) {
            return modelData.derive()
                    .with(VARIANT_INDEX, variantIndex)
                    .build();
        }

        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && pos != null && level != null) {
                boolean isCold = BiomeTemperatureHelper.isColdAt(mc.level, pos);

                if (isCold) {
                    if (hasEnoughSnowNeighbors(level, pos, state)) {
                        long seed = ((long)pos.getX() << 32) | (pos.getZ() & 0xFFFFFFFFL);

                        RandomSource random = RandomSource.create(seed);
                        variantIndex = random.nextInt(NUM_VARIANTS);
                    } else {
                        variantIndex = -1;
                    }
                }
            }
        } catch (Exception e) {

        }

        return modelData.derive()
                .with(VARIANT_INDEX, variantIndex)
                .build();
    }

    private static boolean hasEnoughSnowNeighbors(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockPos checkPos = pos;
        if (state != null && state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                checkPos = pos.below();
            }
        }

        int snowCount = 0;
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

        for (Direction direction : directions) {
            if (hasSnowAdjacent(level, checkPos, direction)) {
                snowCount++;
                if (snowCount >= REQUIRED_SNOW_NEIGHBORS) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasSnowAdjacent(BlockAndTintGetter level, BlockPos plantPos, Direction direction) {
        BlockPos checkPos = plantPos.relative(direction);

        int plantChunkX = plantPos.getX() >> 4;
        int plantChunkZ = plantPos.getZ() >> 4;
        int checkChunkX = checkPos.getX() >> 4;
        int checkChunkZ = checkPos.getZ() >> 4;

        if (plantChunkX != checkChunkX || plantChunkZ != checkChunkZ) {
            return false;
        }

        if (!isChunkLoaded(level, checkPos)) {
            return false;
        }

        try {
            BlockState checkState = level.getBlockState(checkPos);
            return checkState.is(BlockTags.SNOW);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isChunkLoaded(BlockAndTintGetter level, BlockPos pos) {
        try {
            if (level instanceof net.minecraft.client.multiplayer.ClientLevel clientLevel) {
                ChunkAccess chunk = clientLevel.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
                return chunk != null;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return normalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return normalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return normalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return normalModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return normalModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return normalModel.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return normalModel.getOverrides();
    }
}