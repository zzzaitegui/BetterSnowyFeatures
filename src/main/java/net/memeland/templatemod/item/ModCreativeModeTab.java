package net.memeland.templatemod.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab TEMPLATE_TAB = new CreativeModeTab("templatetab") {

        @Override
        public ItemStack makeIcon() {
            return null; // Replace with an item stack to represent the tab icon
        }

        // This method is for ordering the items that the tabs contains
        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            super.fillItemList(pItems);
        }
    };
}
