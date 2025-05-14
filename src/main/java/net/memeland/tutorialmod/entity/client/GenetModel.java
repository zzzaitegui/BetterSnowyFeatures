package net.memeland.tutorialmod.entity.client;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.entity.custom.GenetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GenetModel extends AnimatedGeoModel<GenetEntity> {
    @Override
    public ResourceLocation getModelResource(GenetEntity object) {
        return ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "geo/genet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GenetEntity object) {
        return ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "textures/entity/genet_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GenetEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "animations/animation.genet.json");
    }
}
