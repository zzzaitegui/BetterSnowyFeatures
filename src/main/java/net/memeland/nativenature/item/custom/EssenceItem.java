package net.memeland.nativenature.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EssenceItem extends Item {

    public EssenceItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        // Return the essence so it's not consumed
        return itemStack.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}