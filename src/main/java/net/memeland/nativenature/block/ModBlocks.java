package net.memeland.nativenature.block;

import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.block.custom.*;
import net.memeland.nativenature.fluid.ModFluids;
import net.memeland.nativenature.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NativeNatureMod.MOD_ID);

    // COLD ESSENCE BLOCKS
    public static final RegistryObject<Block> COLD_OAK_LEAVES = registerBlock("cold_oak_leaves",
            () -> new TintedLeavesBlock("cold"));
    public static final RegistryObject<Block> COLD_JUNGLE_LEAVES = registerBlock("cold_jungle_leaves",
            () -> new TintedLeavesBlock("cold"));
    public static final RegistryObject<Block> COLD_ACACIA_LEAVES = registerBlock("cold_acacia_leaves",
            () -> new TintedLeavesBlock("cold"));
    public static final RegistryObject<Block> COLD_DARK_OAK_LEAVES = registerBlock("cold_dark_oak_leaves",
            () -> new TintedLeavesBlock("cold"));
    public static final RegistryObject<Block> COLD_MANGROVE_LEAVES = registerBlock("cold_mangrove_leaves",
            () -> new TintedLeavesBlock("cold"));
    public static final RegistryObject<Block> COLD_GRASS_BLOCK = registerBlock("cold_grass_block",
            () -> new TintedGrassBlock("cold"));
    public static final RegistryObject<Block> COLD_GRASS = registerBlock("cold_grass",
            () -> new TintedPlantBlock("cold"));
    public static final RegistryObject<Block> COLD_TALL_GRASS = registerBlock("cold_tall_grass",
            () -> new TintedDoublePlantBlock("cold"));
    public static final RegistryObject<Block> COLD_SUGAR_CANE = registerBlock("cold_sugar_cane",
            () -> new TintedSugarCaneBlock("cold"));
    public static final RegistryObject<Block> COLD_VINE = registerBlock("cold_vine",
            () -> new TintedVineBlock("cold"));
    public static final RegistryObject<Block> COLD_FERN = registerBlock("cold_fern",
            () -> new TintedFernBlock("cold"));
    public static final RegistryObject<Block> COLD_LARGE_FERN = registerBlock("cold_large_fern",
            () -> new TintedDoublePlantBlock("cold"));

    // GLACIAL ESSENCE BLOCKS
    public static final RegistryObject<Block> GLACIAL_OAK_LEAVES = registerBlock("glacial_oak_leaves",
            () -> new TintedLeavesBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_JUNGLE_LEAVES = registerBlock("glacial_jungle_leaves",
            () -> new TintedLeavesBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_ACACIA_LEAVES = registerBlock("glacial_acacia_leaves",
            () -> new TintedLeavesBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_DARK_OAK_LEAVES = registerBlock("glacial_dark_oak_leaves",
            () -> new TintedLeavesBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_MANGROVE_LEAVES = registerBlock("glacial_mangrove_leaves",
            () -> new TintedLeavesBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_GRASS_BLOCK = registerBlock("glacial_grass_block",
            () -> new TintedGrassBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_GRASS = registerBlock("glacial_grass",
            () -> new TintedPlantBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_TALL_GRASS = registerBlock("glacial_tall_grass",
            () -> new TintedDoublePlantBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_SUGAR_CANE = registerBlock("glacial_sugar_cane",
            () -> new TintedSugarCaneBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_VINE = registerBlock("glacial_vine",
            () -> new TintedVineBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_FERN = registerBlock("glacial_fern",
            () -> new TintedFernBlock("glacial"));
    public static final RegistryObject<Block> GLACIAL_LARGE_FERN = registerBlock("glacial_large_fern",
            () -> new TintedDoublePlantBlock("glacial"));

    // HIGHLANDS ESSENCE BLOCKS
    public static final RegistryObject<Block> HIGHLANDS_OAK_LEAVES = registerBlock("highlands_oak_leaves",
            () -> new TintedLeavesBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_JUNGLE_LEAVES = registerBlock("highlands_jungle_leaves",
            () -> new TintedLeavesBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_ACACIA_LEAVES = registerBlock("highlands_acacia_leaves",
            () -> new TintedLeavesBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_DARK_OAK_LEAVES = registerBlock("highlands_dark_oak_leaves",
            () -> new TintedLeavesBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_MANGROVE_LEAVES = registerBlock("highlands_mangrove_leaves",
            () -> new TintedLeavesBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_GRASS_BLOCK = registerBlock("highlands_grass_block",
            () -> new TintedGrassBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_GRASS = registerBlock("highlands_grass",
            () -> new TintedPlantBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_TALL_GRASS = registerBlock("highlands_tall_grass",
            () -> new TintedDoublePlantBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_SUGAR_CANE = registerBlock("highlands_sugar_cane",
            () -> new TintedSugarCaneBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_VINE = registerBlock("highlands_vine",
            () -> new TintedVineBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_FERN = registerBlock("highlands_fern",
            () -> new TintedFernBlock("highlands"));
    public static final RegistryObject<Block> HIGHLANDS_LARGE_FERN = registerBlock("highlands_large_fern",
            () -> new TintedDoublePlantBlock("highlands"));

    // TEMPERATE ESSENCE BLOCKS
    public static final RegistryObject<Block> TEMPERATE_OAK_LEAVES = registerBlock("temperate_oak_leaves",
            () -> new TintedLeavesBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_JUNGLE_LEAVES = registerBlock("temperate_jungle_leaves",
            () -> new TintedLeavesBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_ACACIA_LEAVES = registerBlock("temperate_acacia_leaves",
            () -> new TintedLeavesBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_DARK_OAK_LEAVES = registerBlock("temperate_dark_oak_leaves",
            () -> new TintedLeavesBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_MANGROVE_LEAVES = registerBlock("temperate_mangrove_leaves",
            () -> new TintedLeavesBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_GRASS_BLOCK = registerBlock("temperate_grass_block",
            () -> new TintedGrassBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_GRASS = registerBlock("temperate_grass",
            () -> new TintedPlantBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_TALL_GRASS = registerBlock("temperate_tall_grass",
            () -> new TintedDoublePlantBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_SUGAR_CANE = registerBlock("temperate_sugar_cane",
            () -> new TintedSugarCaneBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_VINE = registerBlock("temperate_vine",
            () -> new TintedVineBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_FERN = registerBlock("temperate_fern",
            () -> new TintedFernBlock("temperate"));
    public static final RegistryObject<Block> TEMPERATE_LARGE_FERN = registerBlock("temperate_large_fern",
            () -> new TintedDoublePlantBlock("temperate"));

    // RIVER ESSENCE BLOCKS
    public static final RegistryObject<Block> RIVER_OAK_LEAVES = registerBlock("river_oak_leaves",
            () -> new TintedLeavesBlock("river"));
    public static final RegistryObject<Block> RIVER_JUNGLE_LEAVES = registerBlock("river_jungle_leaves",
            () -> new TintedLeavesBlock("river"));
    public static final RegistryObject<Block> RIVER_ACACIA_LEAVES = registerBlock("river_acacia_leaves",
            () -> new TintedLeavesBlock("river"));
    public static final RegistryObject<Block> RIVER_DARK_OAK_LEAVES = registerBlock("river_dark_oak_leaves",
            () -> new TintedLeavesBlock("river"));
    public static final RegistryObject<Block> RIVER_MANGROVE_LEAVES = registerBlock("river_mangrove_leaves",
            () -> new TintedLeavesBlock("river"));
    public static final RegistryObject<Block> RIVER_GRASS_BLOCK = registerBlock("river_grass_block",
            () -> new TintedGrassBlock("river"));
    public static final RegistryObject<Block> RIVER_GRASS = registerBlock("river_grass",
            () -> new TintedPlantBlock("river"));
    public static final RegistryObject<Block> RIVER_TALL_GRASS = registerBlock("river_tall_grass",
            () -> new TintedDoublePlantBlock("river"));
    public static final RegistryObject<Block> RIVER_SUGAR_CANE = registerBlock("river_sugar_cane",
            () -> new TintedSugarCaneBlock("river"));
    public static final RegistryObject<Block> RIVER_VINE = registerBlock("river_vine",
            () -> new TintedVineBlock("river"));
    public static final RegistryObject<Block> RIVER_FERN = registerBlock("river_fern",
            () -> new TintedFernBlock("river"));
    public static final RegistryObject<Block> RIVER_LARGE_FERN = registerBlock("river_large_fern",
            () -> new TintedDoublePlantBlock("river"));

    // ARID ESSENCE BLOCKS
    public static final RegistryObject<Block> ARID_OAK_LEAVES = registerBlock("arid_oak_leaves",
            () -> new TintedLeavesBlock("arid"));
    public static final RegistryObject<Block> ARID_JUNGLE_LEAVES = registerBlock("arid_jungle_leaves",
            () -> new TintedLeavesBlock("arid"));
    public static final RegistryObject<Block> ARID_ACACIA_LEAVES = registerBlock("arid_acacia_leaves",
            () -> new TintedLeavesBlock("arid"));
    public static final RegistryObject<Block> ARID_DARK_OAK_LEAVES = registerBlock("arid_dark_oak_leaves",
            () -> new TintedLeavesBlock("arid"));
    public static final RegistryObject<Block> ARID_MANGROVE_LEAVES = registerBlock("arid_mangrove_leaves",
            () -> new TintedLeavesBlock("arid"));
    public static final RegistryObject<Block> ARID_GRASS_BLOCK = registerBlock("arid_grass_block",
            () -> new TintedGrassBlock("arid"));
    public static final RegistryObject<Block> ARID_GRASS = registerBlock("arid_grass",
            () -> new TintedPlantBlock("arid"));
    public static final RegistryObject<Block> ARID_TALL_GRASS = registerBlock("arid_tall_grass",
            () -> new TintedDoublePlantBlock("arid"));
    public static final RegistryObject<Block> ARID_SUGAR_CANE = registerBlock("arid_sugar_cane",
            () -> new TintedSugarCaneBlock("arid"));
    public static final RegistryObject<Block> ARID_VINE = registerBlock("arid_vine",
            () -> new TintedVineBlock("arid"));
    public static final RegistryObject<Block> ARID_FERN = registerBlock("arid_fern",
            () -> new TintedFernBlock("arid"));
    public static final RegistryObject<Block> ARID_LARGE_FERN = registerBlock("arid_large_fern",
            () -> new TintedDoublePlantBlock("arid"));

    // DRY ESSENCE BLOCKS
    public static final RegistryObject<Block> DRY_OAK_LEAVES = registerBlock("dry_oak_leaves",
            () -> new TintedLeavesBlock("dry"));
    public static final RegistryObject<Block> DRY_JUNGLE_LEAVES = registerBlock("dry_jungle_leaves",
            () -> new TintedLeavesBlock("dry"));
    public static final RegistryObject<Block> DRY_ACACIA_LEAVES = registerBlock("dry_acacia_leaves",
            () -> new TintedLeavesBlock("dry"));
    public static final RegistryObject<Block> DRY_DARK_OAK_LEAVES = registerBlock("dry_dark_oak_leaves",
            () -> new TintedLeavesBlock("dry"));
    public static final RegistryObject<Block> DRY_MANGROVE_LEAVES = registerBlock("dry_mangrove_leaves",
            () -> new TintedLeavesBlock("dry"));
    public static final RegistryObject<Block> DRY_GRASS_BLOCK = registerBlock("dry_grass_block",
            () -> new TintedGrassBlock("dry"));
    public static final RegistryObject<Block> DRY_GRASS = registerBlock("dry_grass",
            () -> new TintedPlantBlock("dry"));
    public static final RegistryObject<Block> DRY_TALL_GRASS = registerBlock("dry_tall_grass",
            () -> new TintedDoublePlantBlock("dry"));
    public static final RegistryObject<Block> DRY_SUGAR_CANE = registerBlock("dry_sugar_cane",
            () -> new TintedSugarCaneBlock("dry"));
    public static final RegistryObject<Block> DRY_VINE = registerBlock("dry_vine",
            () -> new TintedVineBlock("dry"));
    public static final RegistryObject<Block> DRY_FERN = registerBlock("dry_fern",
            () -> new TintedFernBlock("dry"));
    public static final RegistryObject<Block> DRY_LARGE_FERN = registerBlock("dry_large_fern",
            () -> new TintedDoublePlantBlock("dry"));

    // HOT ESSENCE BLOCKS
    public static final RegistryObject<Block> HOT_OAK_LEAVES = registerBlock("hot_oak_leaves",
            () -> new TintedLeavesBlock("hot"));
    public static final RegistryObject<Block> HOT_JUNGLE_LEAVES = registerBlock("hot_jungle_leaves",
            () -> new TintedLeavesBlock("hot"));
    public static final RegistryObject<Block> HOT_ACACIA_LEAVES = registerBlock("hot_acacia_leaves",
            () -> new TintedLeavesBlock("hot"));
    public static final RegistryObject<Block> HOT_DARK_OAK_LEAVES = registerBlock("hot_dark_oak_leaves",
            () -> new TintedLeavesBlock("hot"));
    public static final RegistryObject<Block> HOT_MANGROVE_LEAVES = registerBlock("hot_mangrove_leaves",
            () -> new TintedLeavesBlock("hot"));
    public static final RegistryObject<Block> HOT_GRASS_BLOCK = registerBlock("hot_grass_block",
            () -> new TintedGrassBlock("hot"));
    public static final RegistryObject<Block> HOT_GRASS = registerBlock("hot_grass",
            () -> new TintedPlantBlock("hot"));
    public static final RegistryObject<Block> HOT_TALL_GRASS = registerBlock("hot_tall_grass",
            () -> new TintedDoublePlantBlock("hot"));
    public static final RegistryObject<Block> HOT_SUGAR_CANE = registerBlock("hot_sugar_cane",
            () -> new TintedSugarCaneBlock("hot"));
    public static final RegistryObject<Block> HOT_VINE = registerBlock("hot_vine",
            () -> new TintedVineBlock("hot"));
    public static final RegistryObject<Block> HOT_FERN = registerBlock("hot_fern",
            () -> new TintedFernBlock("hot"));
    public static final RegistryObject<Block> HOT_LARGE_FERN = registerBlock("hot_large_fern",
            () -> new TintedDoublePlantBlock("hot"));

    // WETLANDS ESSENCE BLOCKS
    public static final RegistryObject<Block> WETLANDS_OAK_LEAVES = registerBlock("wetlands_oak_leaves",
            () -> new TintedLeavesBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_JUNGLE_LEAVES = registerBlock("wetlands_jungle_leaves",
            () -> new TintedLeavesBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_ACACIA_LEAVES = registerBlock("wetlands_acacia_leaves",
            () -> new TintedLeavesBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_DARK_OAK_LEAVES = registerBlock("wetlands_dark_oak_leaves",
            () -> new TintedLeavesBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_MANGROVE_LEAVES = registerBlock("wetlands_mangrove_leaves",
            () -> new TintedLeavesBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_GRASS_BLOCK = registerBlock("wetlands_grass_block",
            () -> new TintedGrassBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_GRASS = registerBlock("wetlands_grass",
            () -> new TintedPlantBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_TALL_GRASS = registerBlock("wetlands_tall_grass",
            () -> new TintedDoublePlantBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_SUGAR_CANE = registerBlock("wetlands_sugar_cane",
            () -> new TintedSugarCaneBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_VINE = registerBlock("wetlands_vine",
            () -> new TintedVineBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_FERN = registerBlock("wetlands_fern",
            () -> new TintedFernBlock("wetlands"));
    public static final RegistryObject<Block> WETLANDS_LARGE_FERN = registerBlock("wetlands_large_fern",
            () -> new TintedDoublePlantBlock("wetlands"));

    // CONTINENTAL ESSENCE BLOCKS
    public static final RegistryObject<Block> CONTINENTAL_OAK_LEAVES = registerBlock("continental_oak_leaves",
            () -> new TintedLeavesBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_JUNGLE_LEAVES = registerBlock("continental_jungle_leaves",
            () -> new TintedLeavesBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_ACACIA_LEAVES = registerBlock("continental_acacia_leaves",
            () -> new TintedLeavesBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_DARK_OAK_LEAVES = registerBlock("continental_dark_oak_leaves",
            () -> new TintedLeavesBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_MANGROVE_LEAVES = registerBlock("continental_mangrove_leaves",
            () -> new TintedLeavesBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_GRASS_BLOCK = registerBlock("continental_grass_block",
            () -> new TintedGrassBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_GRASS = registerBlock("continental_grass",
            () -> new TintedPlantBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_TALL_GRASS = registerBlock("continental_tall_grass",
            () -> new TintedDoublePlantBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_SUGAR_CANE = registerBlock("continental_sugar_cane",
            () -> new TintedSugarCaneBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_VINE = registerBlock("continental_vine",
            () -> new TintedVineBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_FERN = registerBlock("continental_fern",
            () -> new TintedFernBlock("continental"));
    public static final RegistryObject<Block> CONTINENTAL_LARGE_FERN = registerBlock("continental_large_fern",
            () -> new TintedDoublePlantBlock("continental"));

    // STYGIAN ESSENCE BLOCKS
    public static final RegistryObject<Block> STYGIAN_OAK_LEAVES = registerBlock("stygian_oak_leaves",
            () -> new TintedLeavesBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_JUNGLE_LEAVES = registerBlock("stygian_jungle_leaves",
            () -> new TintedLeavesBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_ACACIA_LEAVES = registerBlock("stygian_acacia_leaves",
            () -> new TintedLeavesBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_DARK_OAK_LEAVES = registerBlock("stygian_dark_oak_leaves",
            () -> new TintedLeavesBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_MANGROVE_LEAVES = registerBlock("stygian_mangrove_leaves",
            () -> new TintedLeavesBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_GRASS_BLOCK = registerBlock("stygian_grass_block",
            () -> new TintedGrassBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_GRASS = registerBlock("stygian_grass",
            () -> new TintedPlantBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_TALL_GRASS = registerBlock("stygian_tall_grass",
            () -> new TintedDoublePlantBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_SUGAR_CANE = registerBlock("stygian_sugar_cane",
            () -> new TintedSugarCaneBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_VINE = registerBlock("stygian_vine",
            () -> new TintedVineBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_FERN = registerBlock("stygian_fern",
            () -> new TintedFernBlock("stygian"));
    public static final RegistryObject<Block> STYGIAN_LARGE_FERN = registerBlock("stygian_large_fern",
            () -> new TintedDoublePlantBlock("stygian"));

    // TROPICAL ESSENCE BLOCKS
    public static final RegistryObject<Block> TROPICAL_OAK_LEAVES = registerBlock("tropical_oak_leaves",
            () -> new TintedLeavesBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_JUNGLE_LEAVES = registerBlock("tropical_jungle_leaves",
            () -> new TintedLeavesBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_ACACIA_LEAVES = registerBlock("tropical_acacia_leaves",
            () -> new TintedLeavesBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_DARK_OAK_LEAVES = registerBlock("tropical_dark_oak_leaves",
            () -> new TintedLeavesBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_MANGROVE_LEAVES = registerBlock("tropical_mangrove_leaves",
            () -> new TintedLeavesBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_GRASS_BLOCK = registerBlock("tropical_grass_block",
            () -> new TintedGrassBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_GRASS = registerBlock("tropical_grass",
            () -> new TintedPlantBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_TALL_GRASS = registerBlock("tropical_tall_grass",
            () -> new TintedDoublePlantBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_SUGAR_CANE = registerBlock("tropical_sugar_cane",
            () -> new TintedSugarCaneBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_VINE = registerBlock("tropical_vine",
            () -> new TintedVineBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_FERN = registerBlock("tropical_fern",
            () -> new TintedFernBlock("tropical"));
    public static final RegistryObject<Block> TROPICAL_LARGE_FERN = registerBlock("tropical_large_fern",
            () -> new TintedDoublePlantBlock("tropical"));

    // GRASSLANDS ESSENCE BLOCKS
    public static final RegistryObject<Block> GRASSLANDS_OAK_LEAVES = registerBlock("grasslands_oak_leaves",
            () -> new TintedLeavesBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_JUNGLE_LEAVES = registerBlock("grasslands_jungle_leaves",
            () -> new TintedLeavesBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_ACACIA_LEAVES = registerBlock("grasslands_acacia_leaves",
            () -> new TintedLeavesBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_DARK_OAK_LEAVES = registerBlock("grasslands_dark_oak_leaves",
            () -> new TintedLeavesBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_MANGROVE_LEAVES = registerBlock("grasslands_mangrove_leaves",
            () -> new TintedLeavesBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_GRASS_BLOCK = registerBlock("grasslands_grass_block",
            () -> new TintedGrassBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_GRASS = registerBlock("grasslands_grass",
            () -> new TintedPlantBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_TALL_GRASS = registerBlock("grasslands_tall_grass",
            () -> new TintedDoublePlantBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_SUGAR_CANE = registerBlock("grasslands_sugar_cane",
            () -> new TintedSugarCaneBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_VINE = registerBlock("grasslands_vine",
            () -> new TintedVineBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_FERN = registerBlock("grasslands_fern",
            () -> new TintedFernBlock("grasslands"));
    public static final RegistryObject<Block> GRASSLANDS_LARGE_FERN = registerBlock("grasslands_large_fern",
            () -> new TintedDoublePlantBlock("grasslands"));

    // FOREST ESSENCE BLOCKS
    public static final RegistryObject<Block> FOREST_OAK_LEAVES = registerBlock("forest_oak_leaves",
            () -> new TintedLeavesBlock("forest"));
    public static final RegistryObject<Block> FOREST_JUNGLE_LEAVES = registerBlock("forest_jungle_leaves",
            () -> new TintedLeavesBlock("forest"));
    public static final RegistryObject<Block> FOREST_ACACIA_LEAVES = registerBlock("forest_acacia_leaves",
            () -> new TintedLeavesBlock("forest"));
    public static final RegistryObject<Block> FOREST_DARK_OAK_LEAVES = registerBlock("forest_dark_oak_leaves",
            () -> new TintedLeavesBlock("forest"));
    public static final RegistryObject<Block> FOREST_MANGROVE_LEAVES = registerBlock("forest_mangrove_leaves",
            () -> new TintedLeavesBlock("forest"));
    public static final RegistryObject<Block> FOREST_GRASS_BLOCK = registerBlock("forest_grass_block",
            () -> new TintedGrassBlock("forest"));
    public static final RegistryObject<Block> FOREST_GRASS = registerBlock("forest_grass",
            () -> new TintedPlantBlock("forest"));
    public static final RegistryObject<Block> FOREST_TALL_GRASS = registerBlock("forest_tall_grass",
            () -> new TintedDoublePlantBlock("forest"));
    public static final RegistryObject<Block> FOREST_SUGAR_CANE = registerBlock("forest_sugar_cane",
            () -> new TintedSugarCaneBlock("forest"));
    public static final RegistryObject<Block> FOREST_VINE = registerBlock("forest_vine",
            () -> new TintedVineBlock("forest"));
    public static final RegistryObject<Block> FOREST_FERN = registerBlock("forest_fern",
            () -> new TintedFernBlock("forest"));
    public static final RegistryObject<Block> FOREST_LARGE_FERN = registerBlock("forest_large_fern",
            () -> new TintedDoublePlantBlock("forest"));

    // RAINFOREST ESSENCE BLOCKS
    public static final RegistryObject<Block> RAINFOREST_OAK_LEAVES = registerBlock("rainforest_oak_leaves",
            () -> new TintedLeavesBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_JUNGLE_LEAVES = registerBlock("rainforest_jungle_leaves",
            () -> new TintedLeavesBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_ACACIA_LEAVES = registerBlock("rainforest_acacia_leaves",
            () -> new TintedLeavesBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_DARK_OAK_LEAVES = registerBlock("rainforest_dark_oak_leaves",
            () -> new TintedLeavesBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_MANGROVE_LEAVES = registerBlock("rainforest_mangrove_leaves",
            () -> new TintedLeavesBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_GRASS_BLOCK = registerBlock("rainforest_grass_block",
            () -> new TintedGrassBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_GRASS = registerBlock("rainforest_grass",
            () -> new TintedPlantBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_TALL_GRASS = registerBlock("rainforest_tall_grass",
            () -> new TintedDoublePlantBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_SUGAR_CANE = registerBlock("rainforest_sugar_cane",
            () -> new TintedSugarCaneBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_VINE = registerBlock("rainforest_vine",
            () -> new TintedVineBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_FERN = registerBlock("rainforest_fern",
            () -> new TintedFernBlock("rainforest"));
    public static final RegistryObject<Block> RAINFOREST_LARGE_FERN = registerBlock("rainforest_large_fern",
            () -> new TintedDoublePlantBlock("rainforest"));

    // WASTELAND ESSENCE BLOCKS
    public static final RegistryObject<Block> WASTELAND_OAK_LEAVES = registerBlock("wasteland_oak_leaves",
            () -> new TintedLeavesBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_JUNGLE_LEAVES = registerBlock("wasteland_jungle_leaves",
            () -> new TintedLeavesBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_ACACIA_LEAVES = registerBlock("wasteland_acacia_leaves",
            () -> new TintedLeavesBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_DARK_OAK_LEAVES = registerBlock("wasteland_dark_oak_leaves",
            () -> new TintedLeavesBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_MANGROVE_LEAVES = registerBlock("wasteland_mangrove_leaves",
            () -> new TintedLeavesBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_GRASS_BLOCK = registerBlock("wasteland_grass_block",
            () -> new TintedGrassBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_GRASS = registerBlock("wasteland_grass",
            () -> new TintedPlantBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_TALL_GRASS = registerBlock("wasteland_tall_grass",
            () -> new TintedDoublePlantBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_SUGAR_CANE = registerBlock("wasteland_sugar_cane",
            () -> new TintedSugarCaneBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_VINE = registerBlock("wasteland_vine",
            () -> new TintedVineBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_FERN = registerBlock("wasteland_fern",
            () -> new TintedFernBlock("wasteland"));
    public static final RegistryObject<Block> WASTELAND_LARGE_FERN = registerBlock("wasteland_large_fern",
            () -> new TintedDoublePlantBlock("wasteland"));

    // ARCTIC ESSENCE BLOCKS
    public static final RegistryObject<Block> ARCTIC_GRASS_BLOCK = registerBlock("arctic_grass_block",
            () -> new ArcticGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));

    public static final RegistryObject<Block> ARCTIC_OAK_LEAVES = registerBlock("arctic_oak_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_JUNGLE_LEAVES = registerBlock("arctic_jungle_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_ACACIA_LEAVES = registerBlock("arctic_acacia_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_DARK_OAK_LEAVES = registerBlock("arctic_dark_oak_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_MANGROVE_LEAVES = registerBlock("arctic_mangrove_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_BIRCH_LEAVES = registerBlock("arctic_birch_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_SPRUCE_LEAVES = registerBlock("arctic_spruce_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_AZALEA_LEAVES = registerBlock("arctic_azalea_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_FLOWERING_AZALEA_LEAVES = registerBlock("arctic_flowering_azalea_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.FLOWERING_AZALEA_LEAVES)));
    public static final RegistryObject<Block> ARCTIC_CHERRY_LEAVES = registerBlock("arctic_cherry_leaves",
            () -> new ArcticLeavesBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES)));

    public static final RegistryObject<Block> ARCTIC_GRASS = registerBlock("arctic_grass",
            () -> new ArcticPlantBlock());
    public static final RegistryObject<Block> ARCTIC_FERN = registerBlock("arctic_fern",
            () -> new ArcticPlantBlock());

    public static final RegistryObject<Block> ARCTIC_TALL_GRASS = registerBlock("arctic_tall_grass",
            () -> new ArcticDoublePlantBlock());
    public static final RegistryObject<Block> ARCTIC_LARGE_FERN = registerBlock("arctic_large_fern",
            () -> new ArcticDoublePlantBlock());

    public static final RegistryObject<Block> ARCTIC_VINE = registerBlock("arctic_vine",
            () -> new ArcticVineBlock(BlockBehaviour.Properties.copy(Blocks.VINE)));

    // WATER COLORS
    public static final RegistryObject<LiquidBlock> GLACIAL_WATER_BLOCK = BLOCKS.register("glacial_water_block",
            () -> new LiquidBlock(ModFluids.GLACIAL_WATER_STILL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    public static final RegistryObject<LiquidBlock> RIVER_WATER_BLOCK = BLOCKS.register("river_water_block",
            () -> new LiquidBlock(ModFluids.RIVER_WATER_STILL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    public static final RegistryObject<LiquidBlock> WETLANDS_WATER_BLOCK = BLOCKS.register("wetlands_water_block",
            () -> new LiquidBlock(ModFluids.WETLANDS_WATER_STILL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    public static final RegistryObject<LiquidBlock> RAINFOREST_WATER_BLOCK = BLOCKS.register("rainforest_water_block",
            () -> new LiquidBlock(ModFluids.RAINFOREST_WATER_STILL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    public static final RegistryObject<LiquidBlock> TROPICAL_WATER_BLOCK = BLOCKS.register("tropical_water_block",
            () -> new LiquidBlock(ModFluids.TROPICAL_WATER_STILL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    // Helpers
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}