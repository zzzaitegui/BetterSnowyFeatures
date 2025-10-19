package net.memeland.nativenature.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TintedBlockRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient essence;
    private final Ingredient input;

    public TintedBlockRecipe(ResourceLocation id, Ingredient input, Ingredient essence, ItemStack output) {
        this.id = id;
        this.input = input;
        this.essence = essence;
        this.output = output;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (container.getWidth() != 3 || container.getHeight() != 3) {
            return false;
        }

        ItemStack centerStack = container.getItem(4);
        if (!essence.test(centerStack)) {
            return false;
        }

        int[] surroundingSlots = {0, 1, 2, 3, 5, 6, 7, 8};
        for (int slot : surroundingSlots) {
            ItemStack stack = container.getItem(slot);
            if (!input.test(stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        // Return the essence item from center slot
        ItemStack essenceStack = container.getItem(4);
        if (!essenceStack.isEmpty()) {
            remaining.set(4, essenceStack.copy());
        }

        return remaining;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        ingredients.set(0, input);
        ingredients.set(1, input);
        ingredients.set(2, input);
        ingredients.set(3, input);
        ingredients.set(4, essence); // Center slot
        ingredients.set(5, input);
        ingredients.set(6, input);
        ingredients.set(7, input);
        ingredients.set(8, input);
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TINTED_BLOCK_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.BUILDING;
    }

    public static class Serializer implements RecipeSerializer<TintedBlockRecipe> {
        @Override
        public TintedBlockRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            Ingredient essence = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "essence"));

            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = ShapedRecipe.itemStackFromJson(resultJson);

            return new TintedBlockRecipe(recipeId, input, essence, result);
        }

        @Override
        public TintedBlockRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient essence = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();

            return new TintedBlockRecipe(recipeId, input, essence, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TintedBlockRecipe recipe) {
            recipe.input.toNetwork(buffer);
            recipe.essence.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }
    }
}