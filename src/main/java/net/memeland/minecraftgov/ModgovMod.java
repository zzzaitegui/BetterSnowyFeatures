package net.memeland.minecraftgov;

import net.memeland.minecraftgov.block.ModBlocks;
import net.memeland.minecraftgov.block.entity.ModBlockEntities;
import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.recipe.ModRecipeSerializers;
import net.memeland.minecraftgov.screen.ModMenuTypes;
import net.memeland.minecraftgov.screen.renderer.BallotBoxScreen;
import net.memeland.minecraftgov.screen.renderer.BulletinBoardScreen;
import net.memeland.minecraftgov.screen.renderer.IdCardScreen;
import net.memeland.minecraftgov.screen.renderer.PamphletScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModgovMod.MOD_ID)

public class ModgovMod {

    public static final String MOD_ID = "modgov";

    public ModgovMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        @SuppressWarnings("deprecation")
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.BULLETIN_BOARD_MENU.get(), BulletinBoardScreen::new);
            MenuScreens.register(ModMenuTypes.BALLOT_BOX_MENU.get(), BallotBoxScreen::new);
            MenuScreens.register(ModMenuTypes.ID_CARD_MENU.get(), IdCardScreen::new);
            MenuScreens.register(ModMenuTypes.PAMPHLET_MENU.get(), PamphletScreen::new);

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BALLOT_BOX_BLOCK.get(), RenderType.translucent());
        }
    }
}