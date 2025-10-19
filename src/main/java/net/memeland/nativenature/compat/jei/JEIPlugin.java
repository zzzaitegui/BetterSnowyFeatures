package net.memeland.nativenature.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.recipe.TintedBlockRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(NativeNatureMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();

        List<CraftingRecipe> tintedRecipes = recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING)
                .stream()
                .filter(recipe -> recipe instanceof TintedBlockRecipe)
                .map(recipe -> (CraftingRecipe) recipe)
                .collect(Collectors.toList());

        registration.addRecipes(RecipeTypes.CRAFTING, tintedRecipes);
    }
}