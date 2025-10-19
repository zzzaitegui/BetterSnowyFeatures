package net.memeland.nativenature.recipe;

import net.memeland.nativenature.NativeNatureMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NativeNatureMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<TintedBlockRecipe>> TINTED_BLOCK_SERIALIZER =
            SERIALIZERS.register("tinted_block_crafting", TintedBlockRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}