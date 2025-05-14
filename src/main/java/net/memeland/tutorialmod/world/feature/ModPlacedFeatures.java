package net.memeland.tutorialmod.world.feature;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, TutorialMod.MOD_ID);

    public static final RegistryObject<PlacedFeature> ALMANDINE_PLACED_PLACED = PLACED_FEATURES.register("almandine_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.ALMANDINE_ORE.getHolder().get(),
                    commonOrePlacement(7, // Veins per chunk
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> END_ALMANDINE_PLACED_PLACED = PLACED_FEATURES.register("endstone_almandine_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.ENDSTONE_ALMANDINE_ORE.getHolder().get(),
                    commonOrePlacement(7, // Veins per chunk
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> NETHER_ALMANDINE_PLACED_PLACED = PLACED_FEATURES.register("nether_almandine_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.NETHER_ALMANDINE_ORE.getHolder().get(),
                    commonOrePlacement(7, // Veins per chunk
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> LARCH_CHECKED = PLACED_FEATURES.register("larch_checked",
            () -> new PlacedFeature(ModConfiguredFeatures.LARCH_TREE.getHolder().get(),
                    List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.LARCH_SAPLING.get()))));

    public static final RegistryObject<PlacedFeature> LARCH_PLACED = PLACED_FEATURES.register("larch_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.LARCH_TREE_SPAWN.getHolder().get(),
                    VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2))));

    public static final RegistryObject<PlacedFeature> ALMANDINE_GEODE_PLACED = PLACED_FEATURES.register("almandine_geode_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.ALMANDINE_GEODE.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(40)),
                            BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> MORNING_GLORY_PLACED = PLACED_FEATURES.register("morning_glory_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.MORNING_GLORY.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(32),
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP,
                            BiomeFilter.biome())));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier1) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(i), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(i), placementModifier);
    }
}
