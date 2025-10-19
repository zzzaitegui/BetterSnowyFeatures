package net.memeland.nativenature.event;

import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.block.ModBlocks;
import net.memeland.nativenature.block.custom.*;
import net.memeland.nativenature.util.BiomeColorExtractor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = NativeNatureMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModColorHandlers {

    // BLOCK COLORS
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // GRASS BLOCKS
        event.register((state, level, pos, tintIndex) -> {
            if (state.getBlock() instanceof TintedGrassBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedGrassBlock.class));

        // LEAVES
        event.register((state, level, pos, tintIndex) -> {
            if (state.getBlock() instanceof TintedLeavesBlock tintedBlock) {
                return BiomeColorExtractor.getFoliageColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedLeavesBlock.class));

        // PLANTS
        event.register((state, level, pos, tintIndex) -> {
            Block block = state.getBlock();
            if (block instanceof TintedPlantBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            } else if (block instanceof TintedFernBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedPlantBlock.class, TintedFernBlock.class));

        // TALL GRASS / LARGE FERN
        event.register((state, level, pos, tintIndex) -> {
            if (state.getBlock() instanceof TintedDoublePlantBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedDoublePlantBlock.class));

        // SUGAR CANE
        event.register((state, level, pos, tintIndex) -> {
            if (state.getBlock() instanceof TintedSugarCaneBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedSugarCaneBlock.class));

        // VINES
        event.register((state, level, pos, tintIndex) -> {
            if (state.getBlock() instanceof TintedVineBlock tintedBlock) {
                return BiomeColorExtractor.getFoliageColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedVineBlock.class));
    }

    // ITEM COLORS
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // GRASS BLOCKS
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedGrassBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedGrassBlock.class));

        // LEAVES
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedLeavesBlock tintedBlock) {
                return BiomeColorExtractor.getFoliageColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedLeavesBlock.class));

        // PLANTS
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedPlantBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            } else if (block instanceof TintedFernBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedPlantBlock.class, TintedFernBlock.class));

        // TALL GRASS / LARGE FERN
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedDoublePlantBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedDoublePlantBlock.class));

        // SUGAR CANE
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedSugarCaneBlock tintedBlock) {
                return BiomeColorExtractor.getGrassColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedSugarCaneBlock.class));

        // VINES
        event.register((stack, tintIndex) -> {
            Block block = Block.byItem(stack.getItem());
            if (block instanceof TintedVineBlock tintedBlock) {
                return BiomeColorExtractor.getFoliageColor(tintedBlock.getEssenceName());
            }
            return -1;
        }, getAllBlocksOfType(TintedVineBlock.class));
    }

    @SafeVarargs
    private static Block[] getAllBlocksOfType(Class<? extends Block>... blockClasses) {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> {
                    for (Class<? extends Block> blockClass : blockClasses) {
                        if (blockClass.isInstance(block)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toArray(Block[]::new);
    }
}