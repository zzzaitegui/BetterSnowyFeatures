package net.memeland.bsf;

import net.memeland.bsf.block.ModBlocks;
import net.memeland.bsf.client.model.SnowOverlayBakedModel;
import net.memeland.bsf.item.ModCreativeModeTab;
import net.memeland.bsf.item.ModItems;
import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(BetterSnowyFeaturesMod.MOD_ID)
public class BetterSnowyFeaturesMod {

    public static final String MOD_ID = "bsf";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public BetterSnowyFeaturesMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModConfig.register();
        ModCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @SuppressWarnings("removal")
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ICICLE.get(), RenderType.translucent());
            });
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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