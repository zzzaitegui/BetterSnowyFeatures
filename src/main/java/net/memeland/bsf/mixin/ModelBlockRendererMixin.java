package net.memeland.bsf.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.memeland.bsf.client.model.SnowyPlantBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    /**
     * Intercepts the translate call that applies block offsets.
     * For snowy plant variants (with the snow layer), we cancel the offset so the snow layer stays centered and aligned with surrounding snow layers.
     * For normal plants or plants without snow, the offset still applies normally.
     * This is only for Vanilla offset.
     */
    @Redirect(
        method = "tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
            ordinal = 0
        )
    )
    private void bsf$cancelOffsetForSnowyPlants(
        PoseStack poseStack,
        double x, double y, double z,
        BlockAndTintGetter level,
        BakedModel model,
        BlockState state,
        BlockPos pos,
        PoseStack capturedPoseStack,
        VertexConsumer vertexConsumer,
        boolean checkSides,
        RandomSource random,
        long seed,
        int overlay,
        ModelData modelData,
        RenderType renderType
    ) {
        if (model instanceof SnowyPlantBakedModel) {
            Integer variantIndex = modelData.get(SnowyPlantBakedModel.VARIANT_INDEX);

            if (variantIndex != null && variantIndex >= 0) {
                return;
            }
        }

        poseStack.translate(x, y, z);
    }
}