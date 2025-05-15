package net.memeland.templatemod.util;

import net.memeland.templatemod.TemplateMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_ALMANDINE_TOOL = tag("needs_template_tool");
    }

    private static TagKey<Block> tag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(TemplateMod.MOD_ID, name));
    }

    private  static TagKey<Block> forgeTag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
    }
}
