package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotWithRestriction extends SlotItemHandler {

    public SlotWithRestriction(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // This slot only accepts ID cards
        return stack.is(ModItems.ID_CARD.get());
    }
}