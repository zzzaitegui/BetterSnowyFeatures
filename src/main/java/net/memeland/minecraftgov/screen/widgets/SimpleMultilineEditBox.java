package net.memeland.minecraftgov.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

        if (this.getValue().length() >= this.maxLength) {
            return false;
        }

        String[] lines = getLines();
        int cursorPos = getCursorPosition();
        CursorInfo cursorInfo = getCursorLineAndPosition(cursorPos, lines);

        if (cursorInfo.lineIndex < lines.length) {
            String currentLine = lines[cursorInfo.lineIndex];
            String lineWithNewChar = currentLine.substring(0, cursorInfo.positionInLine) +
                    codePoint +
                    currentLine.substring(cursorInfo.positionInLine);

            Font font = Minecraft.getInstance().font;
            int maxLineWidth = this.getWidth() - 12;

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

        if (keyCode == 261) { // Delete
            int cursorPos = getCursorPosition();
            String currentText = this.getValue();

            if (cursorPos < currentText.length()) {
                String newText = currentText.substring(0, cursorPos) + currentText.substring(cursorPos + 1);
                this.setValue(newText);
            }
            return true;
        }

        if (keyCode == 259) {// Backspace
            int cursorPos = getCursorPosition();
            String currentText = this.getValue();

            if (cursorPos > 0) {
                String newText = currentText.substring(0, cursorPos - 1) + currentText.substring(cursorPos);
                this.setValue(newText);
                this.setCursorPosition(cursorPos - 1);
            }
            return true;
        }

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
                int maxLineWidth = this.getWidth() - 12;

                if (font.width(lineWithSpaces) <= maxLineWidth) {
                    int pos = getCursorPosition();
                    String text = this.getValue();
                    String newText = text.substring(0, pos) + "    " + text.substring(pos);
                    this.setValue(newText);
                    this.setCursorPosition(pos + 4);
                }
            }
            return true;
        }

        if (keyCode == 265) { // Up arrow
            moveVertically(-1);
            return true;
        }
        if (keyCode == 264) { // Down arrow
            moveVertically(1);
            return true;
        }

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
            int maxLineWidth = this.getWidth() - 12;
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
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) return;

        // Draw background
        int backgroundColor = this.isFocused() ? -1 : -6250336;
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), backgroundColor);

        // Draw border
        int borderColor = this.isFocused() ? -1 : -6250336;
        guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.getWidth() + 1, this.getY(), borderColor);
        guiGraphics.fill(this.getX() - 1, this.getY() + this.getHeight(), this.getX() + this.getWidth() + 1, this.getY() + this.getHeight() + 1, borderColor);
        guiGraphics.fill(this.getX() - 1, this.getY(), this.getX(), this.getY() + this.getHeight(), borderColor);
        guiGraphics.fill(this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() + 1, this.getY() + this.getHeight(), borderColor);

        // Draw multiline text
        String[] lines = getLines();
        Font font = Minecraft.getInstance().font;
        int textColor = 14737632;
        int textX = this.getX() + 4;
        int startY = this.getY() + 4;
        int maxLineWidth = this.getWidth() - 8;

        for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
            String line = lines[i];
            int lineY = startY + (i * lineHeight);
            if (!line.isEmpty()) {
                String displayText = font.plainSubstrByWidth(line, maxLineWidth);
                guiGraphics.drawString(font, displayText, textX, lineY, textColor, false);
            }
        }

        if (this.isFocused() && Minecraft.getInstance().gui.getGuiTicks() / 6 % 2 == 0) {
            int cursorPos = getCursorPosition();
            CursorInfo cursorInfo = getCursorLineAndPosition(cursorPos, lines);

            if (cursorInfo.lineIndex < maxLines && cursorInfo.lineIndex < lines.length) {
                String fullLine = lines[cursorInfo.lineIndex];
                String displayedLine = font.plainSubstrByWidth(fullLine, maxLineWidth);

                if (cursorInfo.positionInLine <= displayedLine.length()) {
                    String lineUpToCursor = displayedLine.substring(0, Math.min(cursorInfo.positionInLine, displayedLine.length()));
                    int cursorX = textX + font.width(lineUpToCursor);
                    int cursorY = startY + (cursorInfo.lineIndex * lineHeight);

                    if (cursorX <= this.getX() + this.getWidth() - 4) {
                        guiGraphics.fill(cursorX, cursorY - 1, cursorX + 1, cursorY + 9, -3092272);
                    }
                }
            }
        }
    }

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

            currentPos = lineEndPos + 1;
        }

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