package net.memeland.tutorialmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.memeland.tutorialmod.TutorialMod;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ThirstHudOverlay {
    private static final ResourceLocation FILLED_THIRST = ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "textures/thirst/filled_thirst.png");
    private static final ResourceLocation EMPTY_THIRST = ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "textures/thirst/empty_thirst.png");

    //Everything inside here gets rendered
    public static IGuiOverlay HUD_THIRST = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {

        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_THIRST);
        // All the empty thirst cups
        for(int i = 0; i < 10; i++) {               // X and Y coords                    // 0 and 0 for the offset   // 12 ad 12 size for the component // 12 ad 12 size of the image we are using
            GuiComponent.blit(poseStack,x - 94 + (i * 8), y - 54,0,0,16,16, 16,16);
        }

        RenderSystem.setShaderTexture(0, FILLED_THIRST);
        for(int i = 0; i < 10; i++) {
            if(ClientThirstData.getPlayerThirst() > i) {
                GuiComponent.blit(poseStack,x - 94 + (i * 8),y - 54,0,0,16,16,
                        16,16);
            } else {
                break;
            }
        }

    });
}
