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

    public static final RegistryObject<RecipeSerializer<DryingTrayRecipe>> DRYING_TRAY_SERIALIZER =
            SERIALIZERS.register("drying", () -> DryingTrayRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
