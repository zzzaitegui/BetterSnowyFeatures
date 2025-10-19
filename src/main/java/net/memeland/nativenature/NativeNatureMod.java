package net.memeland.nativenature;

import net.memeland.nativenature.block.ModBlocks;
import net.memeland.nativenature.block.custom.*;
import net.memeland.nativenature.fluid.ModFluidTypes;
import net.memeland.nativenature.fluid.ModFluids;
import net.memeland.nativenature.item.ModCreativeModeTab;
import net.memeland.nativenature.item.ModItems;
import net.memeland.nativenature.recipe.ModRecipes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(NativeNatureMod.MOD_ID)
public class NativeNatureMod {

    public static final String MOD_ID = "nativenature";

    public NativeNatureMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.COLD_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLACIAL_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HIGHLANDS_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.TEMPERATE_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.RIVER_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARID_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRY_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HOT_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.WETLANDS_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.CONTINENTAL_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.STYGIAN_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.TROPICAL_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRASSLANDS_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.FOREST_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAINFOREST_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.WASTELAND_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.GLACIAL_WATER_STILL.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.GLACIAL_WATER_FLOWING.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.RIVER_WATER_STILL.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.RIVER_WATER_FLOWING.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.WETLANDS_WATER_STILL.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.WETLANDS_WATER_FLOWING.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.RAINFOREST_WATER_STILL.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.RAINFOREST_WATER_FLOWING.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.TROPICAL_WATER_STILL.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.TROPICAL_WATER_FLOWING.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_GRASS_BLOCK.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_OAK_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_JUNGLE_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_ACACIA_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_DARK_OAK_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_MANGROVE_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_BIRCH_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_SPRUCE_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_AZALEA_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_FLOWERING_AZALEA_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_CHERRY_LEAVES.get(), RenderType.cutoutMipped());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_GRASS.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_TALL_GRASS.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_FERN.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_LARGE_FERN.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARCTIC_VINE.get(), RenderType.cutout());

                setPlantRenderLayers();
            });
        }

        private static void setPlantRenderLayers() {
            RenderType cutout = RenderType.cutout();

            // All leave blocks
            ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(block -> block instanceof TintedLeavesBlock)
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, cutout));

            // All plants
            ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(block -> block instanceof TintedPlantBlock ||
                            block instanceof TintedFernBlock ||
                            block instanceof TintedDoublePlantBlock ||
                            block instanceof TintedSugarCaneBlock ||
                            block instanceof TintedVineBlock)
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, cutout));
        }
    }
}