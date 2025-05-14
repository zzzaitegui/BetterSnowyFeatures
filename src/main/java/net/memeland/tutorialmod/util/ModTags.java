package net.memeland.tutorialmod.util;

import net.memeland.tutorialmod.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_ALMANDINE_TOOL = tag("needs_almandine_tool");
    }

    private static TagKey<Block> tag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, name));
    }

    private  static TagKey<Block> forgeTag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
    }
}
