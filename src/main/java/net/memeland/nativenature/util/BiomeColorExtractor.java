package net.memeland.nativenature.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BiomeColorExtractor {
    private static final Map<String, Integer> GRASS_COLORS = new HashMap<>();
    private static final Map<String, Integer> FOLIAGE_COLORS = new HashMap<>();
    private static boolean initialized = false;

    // THIS IS WHERE THE MAGIC HAPPENS
    // Biome-based essences
    private static final Map<String, ResourceKey<Biome>> ESSENCE_BIOMES = Map.ofEntries(
            Map.entry("cold", Biomes.TAIGA),
            Map.entry("glacial", Biomes.SNOWY_TAIGA),
            Map.entry("highlands", Biomes.MEADOW),
            Map.entry("temperate", Biomes.BIRCH_FOREST),
            Map.entry("river", Biomes.RIVER),
            Map.entry("arid", Biomes.SAVANNA),
            Map.entry("dry", Biomes.BADLANDS),
            Map.entry("hot", Biomes.DESERT),
            Map.entry("wetlands", Biomes.SWAMP),
            Map.entry("continental", Biomes.DARK_FOREST),
            Map.entry("tropical", Biomes.JUNGLE),
            Map.entry("grasslands", Biomes.PLAINS),
            Map.entry("forest", Biomes.FOREST)
    );

    // Custom Essences
    // name -> new int[]{grassColor, foliageColor}
    private static final Map<String, int[]> CUSTOM_ESSENCE_COLORS = Map.ofEntries(
            Map.entry("wasteland", new int[]{0x4c3f2d, 0x4c3f2d}),
            Map.entry("rainforest", new int[]{0x82b820, 0x82b820}),
            Map.entry("stygian", new int[]{0x475b3f, 0x475b3f}) // 3e5e3d
    );

    public static void extractColors() {
        if (initialized) return;

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        var biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);

        for (Map.Entry<String, ResourceKey<Biome>> entry : ESSENCE_BIOMES.entrySet()) {
            String essenceName = entry.getKey();
            var biomeHolder = biomeRegistry.getHolderOrThrow(entry.getValue());
            Biome biome = biomeHolder.value();

            int grassColor = biome.getGrassColor(0, 0);
            int foliageColor = biome.getFoliageColor();

            GRASS_COLORS.put(essenceName, grassColor);
            FOLIAGE_COLORS.put(essenceName, foliageColor);
        }

        // CUSTOM COLORS
        for (Map.Entry<String, int[]> entry : CUSTOM_ESSENCE_COLORS.entrySet()) {
            String essenceName = entry.getKey();
            int[] colors = entry.getValue();

            GRASS_COLORS.put(essenceName, colors[0]);  // Grass color
            FOLIAGE_COLORS.put(essenceName, colors[1]); // Foliage color
        }

        initialized = true;
    }

    public static int getGrassColor(String essenceName) {
        if (!initialized) extractColors();
        return GRASS_COLORS.getOrDefault(essenceName, 0x91BD59); // Default to plains
    }

    public static int getFoliageColor(String essenceName) {
        if (!initialized) extractColors();
        return FOLIAGE_COLORS.getOrDefault(essenceName, 0x77AB2F); // Default to generic foliage
    }

    public static void reset() {
        initialized = false;
        GRASS_COLORS.clear();
        FOLIAGE_COLORS.clear();
    }
}