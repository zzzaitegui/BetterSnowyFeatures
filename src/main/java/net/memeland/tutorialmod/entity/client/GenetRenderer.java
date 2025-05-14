package net.memeland.tutorialmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.entity.custom.GenetEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GenetRenderer extends GeoEntityRenderer<GenetEntity> {
    public GenetRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GenetModel());
        this.shadowRadius = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(GenetEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "textures/entity/genet_texture.png");
    }

    @Override
    public RenderType getRenderType(GenetEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        poseStack.scale(1.1f, 1.1f, 1.1f); //TO CHANGE SCALE IF THE ONE MADE IN BLOCKBENCH IS NOT GOOD
        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}
