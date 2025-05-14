package net.memeland.tutorialmod.world.feature;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, TutorialMod.MOD_ID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_ALMANDINE_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ALMANDINE_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ALMANDINE_ORE.get().defaultBlockState())
    ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> ENDSTONE_ALMANDINE_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), ModBlocks.ENDSTONE_ALMANDINE_ORE.get().defaultBlockState())
    ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> NETHER_ALMANDINE_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, ModBlocks.NETHER_ALMANDINE_ORE.get().defaultBlockState())
    ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ALMANDINE_ORE = CONFIGURED_FEATURES.register("almandine_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_ALMANDINE_ORES.get(), 7)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ENDSTONE_ALMANDINE_ORE = CONFIGURED_FEATURES.register("endstone_almandine_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ENDSTONE_ALMANDINE_ORES.get(), 9)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> NETHER_ALMANDINE_ORE = CONFIGURED_FEATURES.register("nether_almandine_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(NETHER_ALMANDINE_ORES.get(), 9)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> LARCH_TREE = CONFIGURED_FEATURES.register("larch_tree",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.LARCH_LOG.get()),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.simple(ModBlocks.LARCH_LEAVES.get()),
                    new SpruceFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(6)),
                    new TwoLayersFeatureSize(1, 0, 2)).build()));

    public static final RegistryObject<ConfiguredFeature<?, ?>> LARCH_TREE_SPAWN = CONFIGURED_FEATURES.register("larch_tree_spawn",
                    () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ModPlacedFeatures.LARCH_CHECKED.getHolder().get(), 0.5F)),
                            ModPlacedFeatures.LARCH_CHECKED.getHolder().get())));

    public static final RegistryObject<ConfiguredFeature<?, ?>> ALMANDINE_GEODE = CONFIGURED_FEATURES.register("almandine_geode",
            () -> new ConfiguredFeature<>(Feature.GEODE,
                    new GeodeConfiguration(new GeodeBlockSettings(
                            BlockStateProvider.simple(Blocks.AIR),
                            BlockStateProvider.simple(Blocks.DEEPSLATE),
                            BlockStateProvider.simple(ModBlocks.DEEPSLATE_ALMANDINE_ORE.get()),
                            BlockStateProvider.simple(Blocks.CALCITE),
                            BlockStateProvider.simple(Blocks.TUFF),
                            List.of(ModBlocks.ALMANDINE_BLOCK.get().defaultBlockState(), ModBlocks.DEEPSLATE_ALMANDINE_ORE.get().defaultBlockState(), ModBlocks.ALMANDINE_BLOCK.get().defaultBlockState()),
                            BlockTags.FEATURES_CANNOT_REPLACE , BlockTags.GEODE_INVALID_BLOCKS),
                            new GeodeLayerSettings(1.7D, 1.2D, 2.5D, 3.5D),
                            new GeodeCrackSettings(0.25D, 1.5D, 1),
                            0.5D, 0.1D, true,
                            UniformInt.of(3, 8),
                            UniformInt.of(2, 6),
                            UniformInt.of(1, 2),
                            -18, 18, 0.075D, 1)));


    public static final RegistryObject<ConfiguredFeature<?, ?>> MORNING_GLORY = CONFIGURED_FEATURES.register("morning_glory",
            () -> new ConfiguredFeature<>(Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MORNING_GLORY.get()))))));


    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
