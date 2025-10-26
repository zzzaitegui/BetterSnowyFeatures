package net.memeland.bsf.util;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModTags {

    @SuppressWarnings("removal")
    public static class Biomes {
        public static final TagKey<Biome> SNOWY_BIOMES =
                TagKey.create(Registries.BIOME, new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "snowy_biomes"));

        public static final TagKey<Biome> SNOW_BLOCK_BIOMES =
                TagKey.create(Registries.BIOME, new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "snow_block_biomes"));
    }
}