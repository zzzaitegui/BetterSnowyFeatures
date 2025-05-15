package net.memeland.templatemod.compat;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.memeland.templatemod.TemplateMod;
import net.memeland.templatemod.recipe.DryingTrayRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEITemplateModPlugin implements mezz.jei.api.IModPlugin {

    public static RecipeType<DryingTrayRecipe> DRYING_TYPE = new RecipeType<>(DryingTrayRecipeCategory.UID, DryingTrayRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TemplateMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DryingTrayRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<DryingTrayRecipe> recipesDrying = rm.getAllRecipesFor(DryingTrayRecipe.Type.INSTANCE);
        registration.addRecipes(DRYING_TYPE, recipesDrying);
    }
}
