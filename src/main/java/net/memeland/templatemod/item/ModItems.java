package net.memeland.templatemod.item;

import net.memeland.templatemod.TemplateMod;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TemplateMod.MOD_ID);

    public static final RegistryObject<Item> ALMANDINE = ITEMS.register("almandine",
            () -> new Item(props()));

    // Helper method to create the creative mode tab properties and reuse it on all items
    private static Item.Properties props() {
        return new Item.Properties().tab(ModCreativeModeTab.TEMPLATE_TAB);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
