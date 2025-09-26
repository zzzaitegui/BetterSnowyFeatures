package net.memeland.minecraftgov.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class CustomPaperEditBox extends EditBox {

    int tickCount = 0;

    public CustomPaperEditBox(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
        // Set paper-like text color
        this.setTextColor(0xFF887858);
        this.setTextColorUneditable(0x9F8B73);
        // Remove the default border
        this.setBordered(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) {
            return;
        }

        // Draw our custom background first
        this.renderCustomBackground(poseStack);

        // Draw text with custom shadow instead of using super.render()
        this.renderTextWithCustomShadow(poseStack);

        // Draw cursor
        this.renderCursor(poseStack);
    }

    private void renderCustomBackground(PoseStack poseStack) {
        // Paper-like background colors
        int backgroundColor = this.isFocused() ? 0xFFF5F0E8 : 0xFFEFE6D8;
        int borderColor = this.isFocused() ? 0xFF8B7355 : 0xFF9F8B73;

        // Draw background
        fill(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, backgroundColor);

        // Draw inset border effect
        // Top and left borders (darker for inset effect)
        fill(poseStack, this.x, this.y, this.x + this.width, this.y + 1, borderColor);
        fill(poseStack, this.x, this.y, this.x + 1, this.y + this.height, borderColor);

        // Bottom and right borders (lighter)
        fill(poseStack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xFFF0E6C8);
        fill(poseStack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFF0E6C8);
    }

    private void renderTextWithCustomShadow(PoseStack poseStack) {
        String text = this.getValue();
        int textColor = 0xFF887858; // Use the same color you set in constructor
        int shadowColor = 0xFFD1BA7D; // AQUÍ PUEDES CAMBIAR EL COLOR DE LA SOMBRA

        // Calculate text position with padding (matching your original offset)
        int textX = this.x + 6; // Same as your left padding
        int textY = this.y + 6; // Same as your top padding

        // Use Minecraft.getInstance().font instead of the private font field
        Font font = Minecraft.getInstance().font;

        if (text.isEmpty() && !this.isFocused()) {
            // Draw placeholder text with custom shadow
            Component hint = this.getMessage();
            font.draw(poseStack, hint, textX + 1, textY + 1, 0xFF9F8B73); // Shadow
            font.draw(poseStack, hint, textX, textY, 0xFFBBAA99); // Main placeholder text
        } else if (!text.isEmpty()) {
            // Handle text scrolling if text is too long
            int maxWidth = this.width - 12; // Account for padding
            String displayText = font.plainSubstrByWidth(text, maxWidth);

            // Draw text with custom shadow
            font.draw(poseStack, displayText, textX + 1, textY + 1, shadowColor); // Shadow
            font.draw(poseStack, displayText, textX, textY, textColor); // Main text
        }
    }

    private void renderCursor(PoseStack poseStack) {
        // Simplified cursor rendering for 1.19.2 compatibility
        if (this.isFocused() && this.tickCount / 6 % 2 == 0) {
            String text = this.getValue();
            int cursorPos = Math.min(this.getCursorPosition(), text.length());

            // Calculate cursor position (matching text position)
            String beforeCursor = text.substring(0, cursorPos);
            int maxWidth = this.width - 12;

            Font font = Minecraft.getInstance().font;
            String visibleBeforeCursor = font.plainSubstrByWidth(beforeCursor, maxWidth);

            int cursorX = this.x + 6 + font.width(visibleBeforeCursor);
            int cursorY = this.y + 6;

            // Draw cursor
            fill(poseStack, cursorX, cursorY - 1, cursorX + 1, cursorY + 9, 0xFF887858);
        }
    }
}