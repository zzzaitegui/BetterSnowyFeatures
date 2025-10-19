package net.memeland.nativenature.item;

import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NativeNatureMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NATIVENATURE_TAB = CREATIVE_MODE_TABS.register("nativenature_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CONTINENTAL_ESSENCE.get()))
                    .title(Component.literal("Native Nature"))
                    .displayItems((parameters, output) -> {
                        // All 17 essences
                        output.accept(ModItems.COLD_ESSENCE.get());
                        output.accept(ModItems.GLACIAL_ESSENCE.get());
                        output.accept(ModItems.HIGHLANDS_ESSENCE.get());
                        output.accept(ModItems.TEMPERATE_ESSENCE.get());
                        output.accept(ModItems.RIVER_ESSENCE.get());
                        output.accept(ModItems.ARID_ESSENCE.get());
                        output.accept(ModItems.DRY_ESSENCE.get());
                        output.accept(ModItems.HOT_ESSENCE.get());
                        output.accept(ModItems.WETLANDS_ESSENCE.get());
                        output.accept(ModItems.CONTINENTAL_ESSENCE.get());
                        output.accept(ModItems.STYGIAN_ESSENCE.get());
                        output.accept(ModItems.TROPICAL_ESSENCE.get());
                        output.accept(ModItems.GRASSLANDS_ESSENCE.get());
                        output.accept(ModItems.FOREST_ESSENCE.get());
                        output.accept(ModItems.RAINFOREST_ESSENCE.get());
                        output.accept(ModItems.WASTELAND_ESSENCE.get());
                        output.accept(ModItems.ARCTIC_ESSENCE.get());

                        // Cold blocks
                        output.accept(ModBlocks.COLD_OAK_LEAVES.get());
                        output.accept(ModBlocks.COLD_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.COLD_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.COLD_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.COLD_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.COLD_GRASS_BLOCK.get());
                        output.accept(ModBlocks.COLD_GRASS.get());
                        output.accept(ModBlocks.COLD_TALL_GRASS.get());
                        output.accept(ModBlocks.COLD_SUGAR_CANE.get());
                        output.accept(ModBlocks.COLD_VINE.get());
                        output.accept(ModBlocks.COLD_FERN.get());
                        output.accept(ModBlocks.COLD_LARGE_FERN.get());

                        // Glacial blocks
                        output.accept(ModBlocks.GLACIAL_OAK_LEAVES.get());
                        output.accept(ModBlocks.GLACIAL_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.GLACIAL_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.GLACIAL_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.GLACIAL_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.GLACIAL_GRASS_BLOCK.get());
                        output.accept(ModBlocks.GLACIAL_GRASS.get());
                        output.accept(ModBlocks.GLACIAL_TALL_GRASS.get());
                        output.accept(ModBlocks.GLACIAL_SUGAR_CANE.get());
                        output.accept(ModBlocks.GLACIAL_VINE.get());
                        output.accept(ModBlocks.GLACIAL_FERN.get());
                        output.accept(ModBlocks.GLACIAL_LARGE_FERN.get());

                        // Highlands blocks
                        output.accept(ModBlocks.HIGHLANDS_OAK_LEAVES.get());
                        output.accept(ModBlocks.HIGHLANDS_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.HIGHLANDS_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.HIGHLANDS_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.HIGHLANDS_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.HIGHLANDS_GRASS_BLOCK.get());
                        output.accept(ModBlocks.HIGHLANDS_GRASS.get());
                        output.accept(ModBlocks.HIGHLANDS_TALL_GRASS.get());
                        output.accept(ModBlocks.HIGHLANDS_SUGAR_CANE.get());
                        output.accept(ModBlocks.HIGHLANDS_VINE.get());
                        output.accept(ModBlocks.HIGHLANDS_FERN.get());
                        output.accept(ModBlocks.HIGHLANDS_LARGE_FERN.get());

                        // Temperate blocks
                        output.accept(ModBlocks.TEMPERATE_OAK_LEAVES.get());
                        output.accept(ModBlocks.TEMPERATE_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.TEMPERATE_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.TEMPERATE_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.TEMPERATE_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.TEMPERATE_GRASS_BLOCK.get());
                        output.accept(ModBlocks.TEMPERATE_GRASS.get());
                        output.accept(ModBlocks.TEMPERATE_TALL_GRASS.get());
                        output.accept(ModBlocks.TEMPERATE_SUGAR_CANE.get());
                        output.accept(ModBlocks.TEMPERATE_VINE.get());
                        output.accept(ModBlocks.TEMPERATE_FERN.get());
                        output.accept(ModBlocks.TEMPERATE_LARGE_FERN.get());

                        // River blocks
                        output.accept(ModBlocks.RIVER_OAK_LEAVES.get());
                        output.accept(ModBlocks.RIVER_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.RIVER_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.RIVER_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.RIVER_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.RIVER_GRASS_BLOCK.get());
                        output.accept(ModBlocks.RIVER_GRASS.get());
                        output.accept(ModBlocks.RIVER_TALL_GRASS.get());
                        output.accept(ModBlocks.RIVER_SUGAR_CANE.get());
                        output.accept(ModBlocks.RIVER_VINE.get());
                        output.accept(ModBlocks.RIVER_FERN.get());
                        output.accept(ModBlocks.RIVER_LARGE_FERN.get());

                        // Arid blocks
                        output.accept(ModBlocks.ARID_OAK_LEAVES.get());
                        output.accept(ModBlocks.ARID_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.ARID_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.ARID_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.ARID_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.ARID_GRASS_BLOCK.get());
                        output.accept(ModBlocks.ARID_GRASS.get());
                        output.accept(ModBlocks.ARID_TALL_GRASS.get());
                        output.accept(ModBlocks.ARID_SUGAR_CANE.get());
                        output.accept(ModBlocks.ARID_VINE.get());
                        output.accept(ModBlocks.ARID_FERN.get());
                        output.accept(ModBlocks.ARID_LARGE_FERN.get());

                        // Dry blocks
                        output.accept(ModBlocks.DRY_OAK_LEAVES.get());
                        output.accept(ModBlocks.DRY_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.DRY_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.DRY_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.DRY_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.DRY_GRASS_BLOCK.get());
                        output.accept(ModBlocks.DRY_GRASS.get());
                        output.accept(ModBlocks.DRY_TALL_GRASS.get());
                        output.accept(ModBlocks.DRY_SUGAR_CANE.get());
                        output.accept(ModBlocks.DRY_VINE.get());
                        output.accept(ModBlocks.DRY_FERN.get());
                        output.accept(ModBlocks.DRY_LARGE_FERN.get());

                        // Wasteland blocks
                        output.accept(ModBlocks.WASTELAND_OAK_LEAVES.get());
                        output.accept(ModBlocks.WASTELAND_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.WASTELAND_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.WASTELAND_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.WASTELAND_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.WASTELAND_GRASS_BLOCK.get());
                        output.accept(ModBlocks.WASTELAND_GRASS.get());
                        output.accept(ModBlocks.WASTELAND_TALL_GRASS.get());
                        output.accept(ModBlocks.WASTELAND_SUGAR_CANE.get());
                        output.accept(ModBlocks.WASTELAND_VINE.get());
                        output.accept(ModBlocks.WASTELAND_FERN.get());
                        output.accept(ModBlocks.WASTELAND_LARGE_FERN.get());
                        
                        // Hot blocks
                        output.accept(ModBlocks.HOT_OAK_LEAVES.get());
                        output.accept(ModBlocks.HOT_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.HOT_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.HOT_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.HOT_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.HOT_GRASS_BLOCK.get());
                        output.accept(ModBlocks.HOT_GRASS.get());
                        output.accept(ModBlocks.HOT_TALL_GRASS.get());
                        output.accept(ModBlocks.HOT_SUGAR_CANE.get());
                        output.accept(ModBlocks.HOT_VINE.get());
                        output.accept(ModBlocks.HOT_FERN.get());
                        output.accept(ModBlocks.HOT_LARGE_FERN.get());

                        // Wetlands blocks
                        output.accept(ModBlocks.WETLANDS_OAK_LEAVES.get());
                        output.accept(ModBlocks.WETLANDS_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.WETLANDS_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.WETLANDS_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.WETLANDS_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.WETLANDS_GRASS_BLOCK.get());
                        output.accept(ModBlocks.WETLANDS_GRASS.get());
                        output.accept(ModBlocks.WETLANDS_TALL_GRASS.get());
                        output.accept(ModBlocks.WETLANDS_SUGAR_CANE.get());
                        output.accept(ModBlocks.WETLANDS_VINE.get());
                        output.accept(ModBlocks.WETLANDS_FERN.get());
                        output.accept(ModBlocks.WETLANDS_LARGE_FERN.get());

                        // Continental blocks
                        output.accept(ModBlocks.CONTINENTAL_OAK_LEAVES.get());
                        output.accept(ModBlocks.CONTINENTAL_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.CONTINENTAL_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.CONTINENTAL_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.CONTINENTAL_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.CONTINENTAL_GRASS_BLOCK.get());
                        output.accept(ModBlocks.CONTINENTAL_GRASS.get());
                        output.accept(ModBlocks.CONTINENTAL_TALL_GRASS.get());
                        output.accept(ModBlocks.CONTINENTAL_SUGAR_CANE.get());
                        output.accept(ModBlocks.CONTINENTAL_VINE.get());
                        output.accept(ModBlocks.CONTINENTAL_FERN.get());
                        output.accept(ModBlocks.CONTINENTAL_LARGE_FERN.get());

                        // Stygian blocks
                        output.accept(ModBlocks.STYGIAN_OAK_LEAVES.get());
                        output.accept(ModBlocks.STYGIAN_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.STYGIAN_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.STYGIAN_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.STYGIAN_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.STYGIAN_GRASS_BLOCK.get());
                        output.accept(ModBlocks.STYGIAN_GRASS.get());
                        output.accept(ModBlocks.STYGIAN_TALL_GRASS.get());
                        output.accept(ModBlocks.STYGIAN_SUGAR_CANE.get());
                        output.accept(ModBlocks.STYGIAN_VINE.get());
                        output.accept(ModBlocks.STYGIAN_FERN.get());
                        output.accept(ModBlocks.STYGIAN_LARGE_FERN.get());

                        // Tropical blocks
                        output.accept(ModBlocks.TROPICAL_OAK_LEAVES.get());
                        output.accept(ModBlocks.TROPICAL_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.TROPICAL_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.TROPICAL_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.TROPICAL_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.TROPICAL_GRASS_BLOCK.get());
                        output.accept(ModBlocks.TROPICAL_GRASS.get());
                        output.accept(ModBlocks.TROPICAL_TALL_GRASS.get());
                        output.accept(ModBlocks.TROPICAL_SUGAR_CANE.get());
                        output.accept(ModBlocks.TROPICAL_VINE.get());
                        output.accept(ModBlocks.TROPICAL_FERN.get());
                        output.accept(ModBlocks.TROPICAL_LARGE_FERN.get());

                        // Grasslands blocks
                        output.accept(ModBlocks.GRASSLANDS_OAK_LEAVES.get());
                        output.accept(ModBlocks.GRASSLANDS_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.GRASSLANDS_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.GRASSLANDS_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.GRASSLANDS_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.GRASSLANDS_GRASS_BLOCK.get());
                        output.accept(ModBlocks.GRASSLANDS_GRASS.get());
                        output.accept(ModBlocks.GRASSLANDS_TALL_GRASS.get());
                        output.accept(ModBlocks.GRASSLANDS_SUGAR_CANE.get());
                        output.accept(ModBlocks.GRASSLANDS_VINE.get());
                        output.accept(ModBlocks.GRASSLANDS_FERN.get());
                        output.accept(ModBlocks.GRASSLANDS_LARGE_FERN.get());

                        // Forest blocks
                        output.accept(ModBlocks.FOREST_OAK_LEAVES.get());
                        output.accept(ModBlocks.FOREST_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.FOREST_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.FOREST_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.FOREST_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.FOREST_GRASS_BLOCK.get());
                        output.accept(ModBlocks.FOREST_GRASS.get());
                        output.accept(ModBlocks.FOREST_TALL_GRASS.get());
                        output.accept(ModBlocks.FOREST_SUGAR_CANE.get());
                        output.accept(ModBlocks.FOREST_VINE.get());
                        output.accept(ModBlocks.FOREST_FERN.get());
                        output.accept(ModBlocks.FOREST_LARGE_FERN.get());

                        // Rainforest blocks
                        output.accept(ModBlocks.RAINFOREST_OAK_LEAVES.get());
                        output.accept(ModBlocks.RAINFOREST_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.RAINFOREST_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.RAINFOREST_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.RAINFOREST_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.RAINFOREST_GRASS_BLOCK.get());
                        output.accept(ModBlocks.RAINFOREST_GRASS.get());
                        output.accept(ModBlocks.RAINFOREST_TALL_GRASS.get());
                        output.accept(ModBlocks.RAINFOREST_SUGAR_CANE.get());
                        output.accept(ModBlocks.RAINFOREST_VINE.get());
                        output.accept(ModBlocks.RAINFOREST_FERN.get());
                        output.accept(ModBlocks.RAINFOREST_LARGE_FERN.get());

                        // Arctic blocks
                        output.accept(ModBlocks.ARCTIC_GRASS_BLOCK.get());
                        output.accept(ModBlocks.ARCTIC_OAK_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_JUNGLE_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_ACACIA_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_DARK_OAK_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_MANGROVE_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_BIRCH_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_SPRUCE_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_AZALEA_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_FLOWERING_AZALEA_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_CHERRY_LEAVES.get());
                        output.accept(ModBlocks.ARCTIC_GRASS.get());
                        output.accept(ModBlocks.ARCTIC_TALL_GRASS.get());
                        output.accept(ModBlocks.ARCTIC_FERN.get());
                        output.accept(ModBlocks.ARCTIC_LARGE_FERN.get());
                        output.accept(ModBlocks.ARCTIC_VINE.get());

                        // Water buckets
                        output.accept(ModItems.RIVER_WATER_BUCKET.get());
                        output.accept(ModItems.GLACIAL_WATER_BUCKET.get());
                        output.accept(ModItems.TROPICAL_WATER_BUCKET.get());
                        output.accept(ModItems.RAINFOREST_WATER_BUCKET.get());
                        output.accept(ModItems.WETLANDS_WATER_BUCKET.get());
                    })
                    .build()
    );
}