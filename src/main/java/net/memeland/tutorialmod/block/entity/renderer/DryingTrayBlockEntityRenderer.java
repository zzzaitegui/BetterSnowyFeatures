package net.memeland.tutorialmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.memeland.tutorialmod.block.custom.DryingTrayBlock;
import net.memeland.tutorialmod.block.entity.DryingTrayBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

// CURRENTLY THE ITEM BEING RENDERED ONLY UPDATES WHEN ACCESSING THE BLOCK ENTITY, EVEN IF THE ITEM COOKS/DRIES IT WON'T UPDATE UNTIL INTERACTING WITH THE BLOCK ENTITY

public class DryingTrayBlockEntityRenderer implements BlockEntityRenderer<DryingTrayBlockEntity> {

    public DryingTrayBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(DryingTrayBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack itemStack =pBlockEntity.getRenderStack();
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.09f, 0.5f);
        pPoseStack.scale(0.35f, 0.35f, 0.35f);
        pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90));

        switch (pBlockEntity.getBlockState().getValue(DryingTrayBlock.FACING)) {
            case NORTH -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(0));
            case SOUTH -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            case EAST -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
            case WEST -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(270));
        }

        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GUI,
                getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
        pPoseStack.popPose();

    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
