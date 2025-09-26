package net.memeland.minecraftgov.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class CustomPaperButton extends Button {

    public CustomPaperButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.NO_TOOLTIP);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // Paper-like colors based on button state
        int backgroundColor = this.active ? (this.isHoveredOrFocused() ? 0xFFE8D4A0 : 0xFFD4C19C) : 0xFFB8A888;
        int borderColor = this.active ? (this.isHoveredOrFocused() ? 0xFF8B7355 : 0xFF6B5B47) : 0xFF5A4F3F;
        int textColor = this.active ? 0xFF887858 : 0xFF6B5B47;

        // Draw button background
        fill(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, backgroundColor);

        // Draw borders for depth effect
        // Top and left borders (lighter for raised effect)
        fill(poseStack, this.x, this.y, this.x + this.width, this.y + 1, 0xFFF0E6C8);
        fill(poseStack, this.x, this.y, this.x + 1, this.y + this.height, 0xFFF0E6C8);

        // Bottom and right borders (darker for raised effect)
        fill(poseStack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, borderColor);
        fill(poseStack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, borderColor);

        // Draw text (centered, with custom shadow)
        int textX = this.x + this.width / 2;
        int textY = this.y + (this.height - 8) / 2;
        Component message = this.getMessage();
        int textWidth = Minecraft.getInstance().font.width(message);

        // Color personalizado para la sombra (marrón más oscuro)
        int shadowColor = 0xFFD1BA7D;

        // Draw shadow first (offset by 1 pixel)
        Minecraft.getInstance().font.draw(poseStack, message,
                textX - textWidth / 2 + 1, textY + 1, shadowColor);
        // Draw main text
        Minecraft.getInstance().font.draw(poseStack, message,
                textX - textWidth / 2, textY, textColor);
    }
}