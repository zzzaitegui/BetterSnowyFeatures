package net.memeland.tutorialmod.event;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.entity.ModBlockEntities;
import net.memeland.tutorialmod.client.ThirstHudOverlay;
import net.memeland.tutorialmod.networking.ModMessages;
import net.memeland.tutorialmod.networking.packet.DrinkC2SPacket;
import net.memeland.tutorialmod.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.memeland.tutorialmod.block.entity.renderer.DryingTrayBlockEntityRenderer;


public class ClientEvents {
    @Mod.EventBusSubscriber(modid= TutorialMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.DRINKING_KEY.consumeClick()) {
                ModMessages.sendToServer(new DrinkC2SPacket()); // We know that client events are always on the client to calling sendToServer will always send from client to server, makes sense
                // KEYBINDS don't exist on the server so key bind related features like this are on client (see InputEvent library is only for client)
            }
        }
    }
    @Mod.EventBusSubscriber(modid= TutorialMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.DRINKING_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("thirst", ThirstHudOverlay.HUD_THIRST);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.DRYING_TRAY.get(), DryingTrayBlockEntityRenderer::new);
        }
    }
}
