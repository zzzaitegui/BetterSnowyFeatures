package net.memeland.tutorialmod.painting;

import net.memeland.tutorialmod.TutorialMod;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, TutorialMod.MOD_ID);

    public static final RegistryObject<PaintingVariant> FOGGY = PAINTING_VARIANTS.register("foggy",
            () -> new PaintingVariant(160, 160));

    public static final RegistryObject<PaintingVariant> NIGHTFOREST = PAINTING_VARIANTS.register("nightforest",
            () -> new PaintingVariant(208, 88));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
