package net.memeland.bsf;

import net.memeland.bsf.block.ModBlocks;
import net.memeland.bsf.client.model.SnowOverlayBakedModel;
import net.memeland.bsf.item.ModCreativeModeTab;
import net.memeland.bsf.item.ModItems;
import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@SuppressWarnings("removal")
@Mod(BetterSnowyFeaturesMod.MOD_ID)
public class BetterSnowyFeaturesMod {

    public static final String MOD_ID = "bsf";

    public BetterSnowyFeaturesMod(IEventBus modEventBus) {
        ModConfigs.register();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ICICLE.get(), RenderType.translucent());
            });
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onWorldUnload(LevelEvent.Unload event) {
            BiomeTemperatureHelper.clearCache();
            BiomeTemperatureHelper.clearSnowBlockCache();
            SnowOverlayBakedModel.clearCache();
        }

        @SubscribeEvent
        public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            BiomeTemperatureHelper.clearCache();
            BiomeTemperatureHelper.clearSnowBlockCache();
            SnowOverlayBakedModel.clearCache();
        }
    }
}