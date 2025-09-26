package net.memeland.minecraftgov.recipe;

import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.item.custom.PamphletItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PamphletCopyRecipe extends CustomRecipe {

    public PamphletCopyRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int signedPamphlets = 0;
        int unsignedPamphlets = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.PAMPHLET.get()) {
                    if (PamphletItem.isSigned(stack)) {
                        signedPamphlets++;
                    } else {
                        unsignedPamphlets++;
                    }
                } else {
                    // Non-pamphlet item found
                    return false;
                }
            }
        }

        // Must have exactly 1 signed pamphlet and at least 1 unsigned pamphlet
        return signedPamphlets == 1 && unsignedPamphlets >= 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        ItemStack signedPamphlet = ItemStack.EMPTY;
        int unsignedCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty() && stack.getItem() == ModItems.PAMPHLET.get()) {
                if (PamphletItem.isSigned(stack)) {
                    signedPamphlet = stack;
                } else {
                    unsignedCount++;
                }
            }
        }

        if (signedPamphlet.isEmpty() || unsignedCount == 0) {
            return ItemStack.EMPTY;
        }

        // Create copies of the signed pamphlet
        int maxCopies = 8;
        int actualCopies = Math.min(unsignedCount, maxCopies);

        ItemStack result = signedPamphlet.copy();
        result.setCount(1 + actualCopies);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PAMPHLET_COPY.get();
    }
}