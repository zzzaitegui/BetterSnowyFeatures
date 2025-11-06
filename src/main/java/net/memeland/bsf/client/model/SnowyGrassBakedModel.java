package net.memeland.bsf.client.model;

import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowyGrassBakedModel implements BakedModel {

    public static final ModelProperty<BlockAndTintGetter> LEVEL_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<BlockPos> POS_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<Boolean> IS_COLD_PROPERTY = new ModelProperty<>();

    private final BakedModel normalModel;
    private final BakedModel snowyModel;

    public SnowyGrassBakedModel(BakedModel normalModel, BakedModel snowyModel) {
        this.normalModel = normalModel;
        this.snowyModel = snowyModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return normalModel.getQuads(state, direction, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData extraData, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        Boolean isCold = extraData.get(IS_COLD_PROPERTY);

        if (isCold != null && isCold) {
            return snowyModel.getQuads(state, direction, random, extraData, renderType);
        }

        return normalModel.getQuads(state, direction, random, extraData, renderType);
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

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        boolean isCold = false;

        try {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.level != null && pos != null) {
                isCold = BiomeTemperatureHelper.isColdAt(mc.level, pos);
            }
        } catch (Exception e) {
        }

        return modelData.derive()
                .with(LEVEL_PROPERTY, level)
                .with(POS_PROPERTY, pos)
                .with(IS_COLD_PROPERTY, isCold)
                .build();
    }
}