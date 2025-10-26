package net.memeland.bsf;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_SNOWY_PLANTS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ICICLES;

    static {
        BUILDER.push("Features");

        ENABLE_SNOWY_PLANTS = BUILDER
                .comment("Enable snowy grass/fern variants in cold biomes")
                .define("enableSnowyPlants", true);

        ENABLE_ICICLES = BUILDER
                .comment("Enable icicle generation on trees in cold biomes")
                .define("enableIcicles", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SuppressWarnings("removal")
    public static void register() {
        ModLoadingContext.get().registerConfig(Type.COMMON, SPEC);
    }
}