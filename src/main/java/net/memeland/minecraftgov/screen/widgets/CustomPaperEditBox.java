package net.memeland.minecraftgov.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class CustomPaperEditBox extends EditBox {

    int tickCount = 0;

    public CustomPaperEditBox(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
        this.setTextColor(0xFF887858);
        this.setTextColorUneditable(0x9F8B73);
        this.setBordered(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) {
            return;
        }

        this.renderCustomBackground(guiGraphics);

        this.renderTextWithCustomShadow(guiGraphics);

        this.renderCursor(guiGraphics);
    }

    private void renderCustomBackground(GuiGraphics guiGraphics) {
        int backgroundColor = this.isFocused() ? 0xFFF5F0E8 : 0xFFEFE6D8;
        int borderColor = this.isFocused() ? 0xFF8B7355 : 0xFF9F8B73;

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), backgroundColor);

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + 1, borderColor);
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), borderColor);

        guiGraphics.fill(this.getX(), this.getY() + this.getHeight() - 1, this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFF0E6C8);
        guiGraphics.fill(this.getX() + this.getWidth() - 1, this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFF0E6C8);
    }

    private void renderTextWithCustomShadow(GuiGraphics guiGraphics) {
        String text = this.getValue();
        int textColor = 0xFF887858;
        int shadowColor = 0xFFD1BA7D; // Shadow color

        int textX = this.getX() + 6;
        int textY = this.getY() + 6;

        Font font = Minecraft.getInstance().font;

        if (text.isEmpty() && !this.isFocused()) {
            Component hint = this.getMessage();
            guiGraphics.drawString(font, hint, textX + 1, textY + 1, 0xFF9F8B73, false); // Shadow
            guiGraphics.drawString(font, hint, textX, textY, 0xFFBBAA99, false);
        } else if (!text.isEmpty()) {
            int maxWidth = this.getWidth() - 12; // Account for padding
            String displayText = font.plainSubstrByWidth(text, maxWidth);

            guiGraphics.drawString(font, displayText, textX + 1, textY + 1, shadowColor, false); // Shadow
            guiGraphics.drawString(font, displayText, textX, textY, textColor, false);
        }
    }

    private void renderCursor(GuiGraphics guiGraphics) {
        if (this.isFocused() && this.tickCount / 6 % 2 == 0) {
            String text = this.getValue();
            int cursorPos = Math.min(this.getCursorPosition(), text.length());

            String beforeCursor = text.substring(0, cursorPos);
            int maxWidth = this.getWidth() - 12;

            Font font = Minecraft.getInstance().font;
            String visibleBeforeCursor = font.plainSubstrByWidth(beforeCursor, maxWidth);

            int cursorX = this.getX() + 6 + font.width(visibleBeforeCursor);
            int cursorY = this.getY() + 6;

            guiGraphics.fill(cursorX, cursorY - 1, cursorX + 1, cursorY + 9, 0xFF887858);
        }
    }
}