package net.memeland.minecraftgov.recipe;

import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.item.custom.PamphletItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PamphletDyeRecipe extends CustomRecipe {

    public PamphletDyeRecipe(ResourceLocation location, CraftingBookCategory category) {
        super(location, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack pamphletStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.PAMPHLET.get()) {
                    if (!pamphletStack.isEmpty()) return false; // Multiple pamphlets
                    pamphletStack = stack;
                } else if (stack.getItem() instanceof DyeItem) {
                    if (!dyeStack.isEmpty()) return false; // Multiple dyes
                    dyeStack = stack;
                } else {
                    return false; // Invalid item
                }
            }
        }

        return !pamphletStack.isEmpty() && !dyeStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack pamphletStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.PAMPHLET.get()) {
                    pamphletStack = stack;
                } else if (stack.getItem() instanceof DyeItem) {
                    dyeStack = stack;
                }
            }
        }

        if (pamphletStack.isEmpty() || dyeStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = pamphletStack.copy();
        DyeItem dye = (DyeItem) dyeStack.getItem();

        // Set color using our custom method
        PamphletItem.setColor(result, dye.getDyeColor());

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PAMPHLET_DYE.get();
    }
}