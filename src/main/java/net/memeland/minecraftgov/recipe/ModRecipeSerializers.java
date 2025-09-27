package net.memeland.minecraftgov.recipe;

import net.memeland.minecraftgov.ModgovMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModgovMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PamphletCopyRecipe>> PAMPHLET_COPY =
            SERIALIZERS.register("pamphlet_copy",
                    () -> new SimpleCraftingRecipeSerializer<>(PamphletCopyRecipe::new));

    public static final RegistryObject<RecipeSerializer<PamphletDyeRecipe>> PAMPHLET_DYE =
            SERIALIZERS.register("pamphlet_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(PamphletDyeRecipe::new));

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}