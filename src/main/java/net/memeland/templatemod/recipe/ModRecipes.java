package net.memeland.templatemod.recipe;

import net.memeland.templatemod.TemplateMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TemplateMod.MOD_ID);

    // Add serializers for the mods recipes here

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
