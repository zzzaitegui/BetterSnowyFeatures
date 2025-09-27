package net.memeland.minecraftgov.item;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.item.custom.IdCardItem;
import net.memeland.minecraftgov.item.custom.PamphletItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModgovMod.MOD_ID);

    public static final RegistryObject<Item> BALLOT = ITEMS.register("ballot",
            () -> new Item(props()));

    public static final RegistryObject<Item> PAMPHLET = ITEMS.register("pamphlet",
            () -> new PamphletItem(props().stacksTo(1)));

    public static final RegistryObject<Item> ID_CARD = ITEMS.register("id_card",
            () -> new IdCardItem(props().stacksTo(1)));

    // Helper method to create the creative mode tab properties and reuse it on all items
    private static Item.Properties props() {
        return new Item.Properties();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
