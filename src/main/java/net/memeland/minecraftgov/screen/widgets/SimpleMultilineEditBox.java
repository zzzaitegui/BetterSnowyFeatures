package net.memeland.minecraftgov.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SimpleMultilineEditBox extends EditBox {
    private final int maxLines;
    private final int lineHeight;
    private int maxLength = 500; // Default, will be updated by setMaxLength()

    public SimpleMultilineEditBox(Font font, int x, int y, int width, int height, Component message, int maxLines) {
        super(font, x, y, width, height, message);
        this.maxLines = maxLines;
        this.lineHeight = font.lineHeight + 2;
    }

    @Override
    public void setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        this.maxLength = maxLength;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isActive()) {
            return false;
        }

        // Check total length
        if (this.getValue().length() >= this.maxLength) {
            return false;
        }

        // Check if adding this character would make the current line too wide
        String[] lines = getLines();
        int cursorPos = getCursorPosition();
        CursorInfo cursorInfo = getCursorLineAndPosition(cursorPos, lines);

        if (cursorInfo.lineIndex < lines.length) {
            String currentLine = lines[cursorInfo.lineIndex];
            String lineWithNewChar = currentLine.substring(0, cursorInfo.positionInLine) +
                    codePoint +
                    currentLine.substring(cursorInfo.positionInLine);

            Font font = Minecraft.getInstance().font;
            int maxLineWidth = this.width - 12; // Account for padding

            // If the line would be too wide, don't allow the character
            if (font.width(lineWithNewChar) > maxLineWidth) {
                return false;
            }
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive()) {
            return false;
        }

        // Handle Delete key manually (keyCode 261)
        if (keyCode == 261) {
            int cursorPos = getCursorPosition();
            String currentText = this.getValue();

            if (cursorPos < currentText.length()) {
                String newText = currentText.substring(0, cursorPos) + currentText.substring(cursorPos + 1);
                this.setValue(newText);
                // Cursor position stays the same
            }
            return true;
        }

        // Handle Backspace key manually (keyCode 259)
        if (keyCode == 259) {
            int cursorPos = getCursorPosition();
            String currentText = this.getValue();

            if (cursorPos > 0) {
                String newText = currentText.substring(0, cursorPos - 1) + currentText.substring(cursorPos);
                this.setValue(newText);
                this.setCursorPosition(cursorPos - 1);
            }
            return true;
        }

        // Handle Enter key to add newlines
        if (keyCode == 257 || keyCode == 13) {
            if (getLines().length < maxLines) {
                int cursorPos = getCursorPosition();
                String currentText = this.getValue();
                String newText = currentText.substring(0, cursorPos) + "\n" + currentText.substring(cursorPos);
                this.setValue(newText);
                this.setCursorPosition(cursorPos + 1);
                return true;
            }
            return true;
        }

        // Handle Tab key for indentation (but check line width)
        if (keyCode == 258) {
            String[] lines = getLines();
            int cursorPos = getCursorPosition();
            CursorInfo cursorInfo = getCursorLineAndPosition(cursorPos, lines);

            if (cursorInfo.lineIndex < lines.length) {
                String currentLine = lines[cursorInfo.lineIndex];
                String lineWithSpaces = currentLine.substring(0, cursorInfo.positionInLine) +
                        "    " +
                        currentLine.substring(cursorInfo.positionInLine);

                Font font = Minecraft.getInstance().font;
                int maxLineWidth = this.width - 12;

                // Only add spaces if line won't be too wide
                if (font.width(lineWithSpaces) <= maxLineWidth) {
                    // Insert spaces manually instead of using charTyped to avoid width conflicts
                    int pos = getCursorPosition();
                    String text = this.getValue();
                    String newText = text.substring(0, pos) + "    " + text.substring(pos);
                    this.setValue(newText);
                    this.setCursorPosition(pos + 4);
                }
            }
            return true;
        }

        // Handle up/down arrow keys for line navigation
        if (keyCode == 265) { // Up arrow
            moveVertically(-1);
            return true;
        }
        if (keyCode == 264) { // Down arrow
            moveVertically(1);
            return true;
        }

        // For other keys (left/right arrows, home, end, etc.), let parent handle them
        // but NOT delete/backspace which we handle above
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void moveVertically(int direction) {
        String[] lines = getLines();
        if (lines.length <= 1) return;

        int cursorPos = getCursorPosition();
        CursorInfo currentInfo = getCursorLineAndPosition(cursorPos, lines);

        int targetLine = Mth.clamp(currentInfo.lineIndex + direction, 0, lines.length - 1);

        if (targetLine != currentInfo.lineIndex) {
            String targetLineText = lines[targetLine];

            // Limit position to what can actually be displayed
            Font font = Minecraft.getInstance().font;
            int maxLineWidth = this.width - 12;
            String displayableText = font.plainSubstrByWidth(targetLineText, maxLineWidth);

            int newPositionInLine = Math.min(currentInfo.positionInLine, displayableText.length());

            int newCursorPos = 0;
            for (int i = 0; i < targetLine; i++) {
                newCursorPos += lines[i].length() + 1;
            }
            newCursorPos += newPositionInLine;

            this.setCursorPosition(Math.min(newCursorPos, this.getValue().length()));
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) return;

        // Draw background
        int backgroundColor = this.isFocused() ? -1 : -6250336;
        fill(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, backgroundColor);

        // Draw border
        int borderColor = this.isFocused() ? -1 : -6250336;
        fill(poseStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y, borderColor);
        fill(poseStack, this.x - 1, this.y + this.height, this.x + this.width + 1, this.y + this.height + 1, borderColor);
        fill(poseStack, this.x - 1, this.y, this.x, this.y + this.height, borderColor);
        fill(poseStack, this.x + this.width, this.y, this.x + this.width + 1, this.y + this.height, borderColor);

        // Draw multiline text
        String[] lines = getLines();
        Font font = Minecraft.getInstance().font;
        int textColor = 14737632;
        int textX = this.x + 4;
        int startY = this.y + 4;
        int maxLineWidth = this.width - 8;

        for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
            String line = lines[i];
            int lineY = startY + (i * lineHeight);
            if (!line.isEmpty()) {
                String displayText = font.plainSubstrByWidth(line, maxLineWidth);
                font.draw(poseStack, displayText, textX, lineY, textColor);
            }
        }

        // Fixed cursor rendering that matches displayed text
        if (this.isFocused() && Minecraft.getInstance().gui.getGuiTicks() / 6 % 2 == 0) {
            int cursorPos = getCursorPosition();
            CursorInfo cursorInfo = getCursorLineAndPosition(cursorPos, lines);

            if (cursorInfo.lineIndex < maxLines && cursorInfo.lineIndex < lines.length) {
                // Get the displayed portion of the line (truncated if too long)
                String fullLine = lines[cursorInfo.lineIndex];
                String displayedLine = font.plainSubstrByWidth(fullLine, maxLineWidth);

                // Only show cursor if it's within the displayed portion
                if (cursorInfo.positionInLine <= displayedLine.length()) {
                    String lineUpToCursor = displayedLine.substring(0, Math.min(cursorInfo.positionInLine, displayedLine.length()));
                    int cursorX = textX + font.width(lineUpToCursor);
                    int cursorY = startY + (cursorInfo.lineIndex * lineHeight);

                    // Only draw cursor if it's within the widget bounds
                    if (cursorX <= this.x + this.width - 4) {
                        fill(poseStack, cursorX, cursorY - 1, cursorX + 1, cursorY + 9, -3092272);
                    }
                }
            }
        }
    }

    // Helper method to find which line the cursor is on and position within that line
    private CursorInfo getCursorLineAndPosition(int cursorPos, String[] lines) {
        int currentPos = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineEndPos = currentPos + line.length();

            if (cursorPos <= lineEndPos) {
                int positionInLine = cursorPos - currentPos;
                String textBeforeCursor = line.substring(0, Math.min(positionInLine, line.length()));
                return new CursorInfo(i, positionInLine, textBeforeCursor);
            }

            currentPos = lineEndPos + 1; // +1 for the newline character
        }

        // Cursor is at the very end
        int lastLineIndex = Math.max(0, lines.length - 1);
        return new CursorInfo(lastLineIndex, lines[lastLineIndex].length(), lines[lastLineIndex]);
    }

    private String[] getLines() {
        String text = this.getValue();
        if (text.isEmpty()) {
            return new String[]{""};
        }
        return text.split("\n", -1);
    }

    public int getMaxLines() {
        return maxLines;
    }

    // Helper class to store cursor information
    private static class CursorInfo {
        final int lineIndex;
        final int positionInLine;
        final String textBeforeCursor;

        CursorInfo(int lineIndex, int positionInLine, String textBeforeCursor) {
            this.lineIndex = lineIndex;
            this.positionInLine = positionInLine;
            this.textBeforeCursor = textBeforeCursor;
        }
    }
}