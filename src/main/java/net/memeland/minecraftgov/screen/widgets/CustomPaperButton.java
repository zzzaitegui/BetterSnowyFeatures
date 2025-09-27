package net.memeland.minecraftgov.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CustomPaperButton extends Button {

    public CustomPaperButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundColor = this.active ? (this.isHoveredOrFocused() ? 0xFFE8D4A0 : 0xFFD4C19C) : 0xFFB8A888;
        int borderColor = this.active ? (this.isHoveredOrFocused() ? 0xFF8B7355 : 0xFF6B5B47) : 0xFF5A4F3F;
        int textColor = this.active ? 0xFF887858 : 0xFF6B5B47;

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), backgroundColor);

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + 1, 0xFFF0E6C8);
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), 0xFFF0E6C8);

        guiGraphics.fill(this.getX(), this.getY() + this.getHeight() - 1, this.getX() + this.getWidth(), this.getY() + this.getHeight(), borderColor);
        guiGraphics.fill(this.getX() + this.getWidth() - 1, this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), borderColor);

        int textX = this.getX() + this.getWidth() / 2;
        int textY = this.getY() + (this.getHeight() - 8) / 2;
        Component message = this.getMessage();
        int textWidth = Minecraft.getInstance().font.width(message);

        int shadowColor = 0xFFD1BA7D;

        guiGraphics.drawString(Minecraft.getInstance().font, message,
                textX - textWidth / 2 + 1, textY + 1, shadowColor, false);
        guiGraphics.drawString(Minecraft.getInstance().font, message,
                textX - textWidth / 2, textY, textColor, false);
    }
}