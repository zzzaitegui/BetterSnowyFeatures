package net.memeland.minecraftgov.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MINECRAFTGOV_TAB = new CreativeModeTab("modgovtab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ID_CARD.get());
        }

        // This method is for ordering the items that the tabs contains
        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            super.fillItemList(pItems);
        }
    };
}
