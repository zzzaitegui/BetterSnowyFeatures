package net.memeland.bsf;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.util.List;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_PLANT_SNOW_LAYERS;
    public static final ForgeConfigSpec.BooleanValue USE_TEMPERATURE_FALLBACK;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SNOW_OVERLAY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SNOWY_BIOMES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SNOWY_BIOMES_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SNOW_BLOCK_BIOMES;

    static {
        BUILDER.push("Features");

        ENABLE_PLANT_SNOW_LAYERS = BUILDER
                .comment("Enable fake snow layers on plants when surrounded by snow blocks",
                        "This creates the plant+snow combined models for grass/ferns in snow")
                .define("enablePlantSnowLayers", true);

        ENABLE_SNOW_OVERLAY = BUILDER
                .comment("Enable snow overlay on blocks with snow on top",
                        "Adds a small snow edge to sides of blocks like stone, dirt, wood when snow is above them",
                        "Only works in cold biomes for performance optimization. It is recommended to use with Embeddium or other optimization mods.")
                .define("enableSnowOverlay", true);

        BUILDER.pop();

        BUILDER.push("Biome Configuration");

        USE_TEMPERATURE_FALLBACK = BUILDER
                .comment("Automatically apply snowy effects to cold biomes based on temperature, useful for large biome mods",
                        "Enable this if you don't want to manually add biomes to the whitelist")
                .define("useTemperatureFallback", false);

        SNOWY_BIOMES = BUILDER
                .comment("Biomes that get snowy effects (leaves, grass blocks, plants)",
                        "Affects: snowy leaves, snowy plant textures, permanently snowy grass blocks, plant+snow layers",
                        "Format: minecraft:snowy_taiga, terralith:alpine_grove, etc.")
                .defineList("snowyBiomes", getDefaultSnowyBiomes(), o -> o instanceof String);

        SNOWY_BIOMES_BLACKLIST = BUILDER
                .comment("Biomes to exclude from snowy effects even if they match temperature fallback")
                .defineList("snowyBiomesBlacklist", List.of(), o -> o instanceof String);

        SNOW_BLOCK_BIOMES = BUILDER
                .comment("Biomes that get white/snowy dirt blocks (this is for rare cases in some biomes feature)",
                        "This is a separate list and is NOT affected by temperature fallback",
                        "Format: minecraft:snowy_plains, etc.")
                .defineList("snowBlockBiomes", getDefaultSnowBlockBiomes(), o -> o instanceof String);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static List<String> getDefaultSnowyBiomes() {
        return List.of(
                "minecraft:snowy_plains",
                "minecraft:ice_spikes",
                "minecraft:snowy_taiga",
                "minecraft:snowy_beach",
                "minecraft:grove",
                "minecraft:snowy_slopes",
                "minecraft:jagged_peaks",
                "minecraft:frozen_peaks",
                "minecraft:frozen_river",
                "minecraft:frozen_ocean",
                "minecraft:deep_frozen_ocean",
                "biomesoplenty:snowy_coniferous_forest",
                "biomesoplenty:snowy_fir_clearing",
                "biomesoplenty:auroral_garden",
                "biomesoplenty:muskeg",
                "biomesoplenty:snowblossom_grove",
                "biomesoplenty:snowy_maple_woods",
                "windswept:snowy_pine_forest",
                "windswept:snowy_chestnut_forest",
                "regions_unexplored:cold_boreal_taiga",
                "regions_unexplored:cold_deciduous_forest",
                "regions_unexplored:frozen_pine_taiga",
                "regions_unexplored:icy_heights",
                "regions_unexplored:spires",
                "terralith:alpine_grove",
                "terralith:emerald_peaks",
                "terralith:frozen_cliffs",
                "terralith:scarlet_mountains",
                "terralith:siberian_grove",
                "terralith:skyland_winter",
                "terralith:snowy_badlands",
                "terralith:wintry_forest",
                "byg:frosted_coniferous_forest",
                "byg:frosted_taiga",
                "byg:howling_peaks",
                "byg:shattered_glacier",
                "biomeswevegone:eroded_borealis",
                "biomeswevegone:frosted_coniferous_forest",
                "biomeswevegone:frosted_taiga",
                "biomeswevegone:howling_peaks"
        );
    }

    private static List<String> getDefaultSnowBlockBiomes() {
        return List.of(
                "minecraft:grove",
                "minecraft:jagged_peaks",
                "minecraft:ice_spikes",
                "minecraft:frozen_peaks",
                "minecraft:snowy_slopes",
                "terralith:alpine_grove",
                "terralith:frozen_cliffs",
                "terralith:wintry_forest"
        );
    }

    @SuppressWarnings("removal")
    public static void register() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, SPEC);
    }
}