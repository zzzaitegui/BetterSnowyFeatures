package net.memeland.tutorialmod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.ModBlocks;
import net.memeland.tutorialmod.recipe.DryingTrayRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DryingTrayRecipeCategory implements IRecipeCategory<DryingTrayRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "drying");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "textures/gui/drying_tray_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public DryingTrayRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0 , 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DRYING_TRAY.get()));
    }

    @Override
    public RecipeType<DryingTrayRecipe> getRecipeType() {
        return JEITutorialModPlugin.DRYING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.compat.tutorialmod.title");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingTrayRecipe dryingTrayRecipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients(dryingTrayRecipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 15).addIngredients(ForgeTypes.FLUID_STACK,
                List.of(dryingTrayRecipe.getFluid())).setFluidRenderer(64000, false, 16 , 61);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 60).addItemStack(dryingTrayRecipe.getResultItem());
    }
}
