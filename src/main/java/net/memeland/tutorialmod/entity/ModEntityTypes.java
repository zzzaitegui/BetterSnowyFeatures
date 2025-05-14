package net.memeland.tutorialmod.entity;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.entity.custom.GenetEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<EntityType<GenetEntity>> GENET =
            ENTITY_TYPES.register("genet",
                    () -> EntityType.Builder.of(GenetEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 0.6f) // HITBOX
                            .build(ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "genet").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
