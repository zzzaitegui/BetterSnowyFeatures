package net.memeland.nativenature.item;

import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.fluid.ModFluids;
import net.memeland.nativenature.item.custom.EssenceItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NativeNatureMod.MOD_ID);

    // Essences
    public static final RegistryObject<Item> COLD_ESSENCE = ITEMS.register("cold_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> GLACIAL_ESSENCE = ITEMS.register("glacial_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> HIGHLANDS_ESSENCE = ITEMS.register("highlands_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> TEMPERATE_ESSENCE = ITEMS.register("temperate_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> RIVER_ESSENCE = ITEMS.register("river_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> ARID_ESSENCE = ITEMS.register("arid_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> DRY_ESSENCE = ITEMS.register("dry_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> HOT_ESSENCE = ITEMS.register("hot_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> WETLANDS_ESSENCE = ITEMS.register("wetlands_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> CONTINENTAL_ESSENCE = ITEMS.register("continental_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> STYGIAN_ESSENCE = ITEMS.register("stygian_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> TROPICAL_ESSENCE = ITEMS.register("tropical_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> GRASSLANDS_ESSENCE = ITEMS.register("grasslands_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> FOREST_ESSENCE = ITEMS.register("forest_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> RAINFOREST_ESSENCE = ITEMS.register("rainforest_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> WASTELAND_ESSENCE = ITEMS.register("wasteland_essence",
            EssenceItem::new);
    public static final RegistryObject<Item> ARCTIC_ESSENCE = ITEMS.register("arctic_essence",
            EssenceItem::new);

    // WATER BUCKETS
    public static final RegistryObject<Item> GLACIAL_WATER_BUCKET = ITEMS.register("glacial_water_bucket",
            () -> new BucketItem(ModFluids.GLACIAL_WATER_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> RIVER_WATER_BUCKET = ITEMS.register("river_water_bucket",
            () -> new BucketItem(ModFluids.RIVER_WATER_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> WETLANDS_WATER_BUCKET = ITEMS.register("wetlands_water_bucket",
            () -> new BucketItem(ModFluids.WETLANDS_WATER_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> RAINFOREST_WATER_BUCKET = ITEMS.register("rainforest_water_bucket",
            () -> new BucketItem(ModFluids.RAINFOREST_WATER_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> TROPICAL_WATER_BUCKET = ITEMS.register("tropical_water_bucket",
            () -> new BucketItem(ModFluids.TROPICAL_WATER_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}