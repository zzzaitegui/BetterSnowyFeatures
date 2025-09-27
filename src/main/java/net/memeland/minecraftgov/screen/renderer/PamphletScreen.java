package net.memeland.minecraftgov.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.UpdatePamphletPacket;
import net.memeland.minecraftgov.networking.packet.SignPamphletPacket;
import net.memeland.minecraftgov.screen.PamphletMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.memeland.minecraftgov.screen.widgets.SimpleMultilineEditBox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PamphletScreen extends AbstractContainerScreen<PamphletMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/gui/pamphlet_gui.png");

    private static final ResourceLocation[] SYMBOL_TEXTURES = new ResourceLocation[25];

    static {
        for (int i = 0; i < 25; i++) {
            SYMBOL_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/misc/symbols/symbol" + (i + 1) + ".png");
        }
    }

    private EditBox titleField;
    private SimpleMultilineEditBox contentField;
    private Button saveButton;
    private Button signButton;

    private List<Integer> selectedSymbols = new ArrayList<>();
    private boolean symbolSelectorOpen = false;

    public PamphletScreen(PamphletMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 384;
        this.imageHeight = 200;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.titleLabelY = 10;
        selectedSymbols.clear();
        selectedSymbols.addAll(menu.getSymbols());

        if (menu.canEdit()) {
            titleField = new EditBox(this.font, x + 32, y + 50, 320, 20, Component.translatable("gui.modgov.pamphlet.title"));
            titleField.setMaxLength(41);
            titleField.setValue(menu.getTitle());
            this.addRenderableWidget(titleField);

            contentField = new SimpleMultilineEditBox(this.font, x + 32, y + 90, 320, 60, Component.translatable("gui.modgov.pamphlet.content"), 5);
            contentField.setMaxLength(500);
            contentField.setValue(menu.getContent());
            this.addRenderableWidget(contentField);

            saveButton = Button.builder(
                    Component.translatable("gui.modgov.save"),
                    button -> save()
            ).bounds(x + 32, y + 160, 60, 20).build();
            this.addRenderableWidget(saveButton);

            signButton = Button.builder(
                    Component.translatable("gui.modgov.sign"),
                    button -> sign()
            ).bounds(x + 102, y + 160, 60, 20).build();
            this.addRenderableWidget(signButton);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (menu.hasColor()) {
            int backgroundColor = menu.getBackgroundColor();
            float r = ((backgroundColor >> 16) & 0xFF) / 255.0f;
            float g = ((backgroundColor >> 8) & 0xFF) / 255.0f;
            float b = (backgroundColor & 0xFF) / 255.0f;
            RenderSystem.setShaderColor(r, g, b, 1.0f);
        } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 384, 200);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        renderSymbolsInHeader(guiGraphics, x, y);

        if (symbolSelectorOpen && menu.canEdit()) {
            renderSymbolSelector(guiGraphics, x, y, mouseX, mouseY);
        }
    }

    private void renderSymbolsInHeader(GuiGraphics guiGraphics, int x, int y) {
        if (selectedSymbols.isEmpty()) return;

        int headerY = y + 15;
        int symbolSize = selectedSymbols.size() == 1 ? 32 : 20; // Later: bigger if only 1 symbol
        int totalWidth = selectedSymbols.size() * (symbolSize + 4) - 4;
        int startX = x + (imageWidth - totalWidth) / 2;

        for (int i = 0; i < selectedSymbols.size(); i++) {
            int symbolId = selectedSymbols.get(i);
            if (symbolId >= 1 && symbolId <= 25) {
                try {
                    int symbolX = startX + i * (symbolSize + 4);
                    guiGraphics.blit(SYMBOL_TEXTURES[symbolId - 1], symbolX, headerY, symbolSize, symbolSize, 0.0f, 0.0f, 16, 16, 16, 16);
                } catch (Exception e) {
                    // Silent fallback
                }
            }
        }
    }

    private void renderSymbolSelector(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        int selectorX = x + 132;
        int selectorY = y - 110;
        int selectorWidth = 120;
        int selectorHeight = 110;

        // Background with border
        int borderColor = menu.hasColor() ? menu.getTextColor() : 0xFF777774;
        int backgroundColor = menu.hasColor() ? menu.getBackgroundColor() : 0xFFDCDAD5;

        guiGraphics.fill(selectorX - 2, selectorY - 2, selectorX + selectorWidth, selectorY + selectorHeight, borderColor); // BORDER
        guiGraphics.fill(selectorX, selectorY, selectorX + selectorWidth - 2, selectorY + selectorHeight - 2, backgroundColor); // BACKGROUND

        int gridStartX = selectorX + 10;
        int gridStartY = selectorY + 5;

        for (int i = 0; i < 25; i++) {
            int gridX = i % 5;
            int gridY = i / 5;
            int symbolX = gridStartX + (gridX * 20);
            int symbolY = gridStartY + (gridY * 20);

            // Background for each symbol slot
            int bgColor = selectedSymbols.contains(i + 1) ? 0xFF90EE90 : 0xFFFFFFFF; // SYMBOL BACKGROUND
            guiGraphics.fill(symbolX, symbolY, symbolX + 18, symbolY + 18, bgColor);

            // Symbol texture
            try {
                guiGraphics.blit(SYMBOL_TEXTURES[i], symbolX + 1, symbolY + 1, 0, 0, 16, 16, 16, 16);
            } catch (Exception e) {
                // Silent fallback
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        int defaultTextColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
        guiGraphics.drawString(this.font, this.title, 8, 9, defaultTextColor, false);

        if (menu.canEdit()) {
            guiGraphics.drawString(this.font, Component.translatable("gui.modgov.pamphlet.title"), 32, 40, defaultTextColor, false);
            guiGraphics.drawString(this.font, Component.translatable("gui.modgov.pamphlet.content"), 32, 80, defaultTextColor, false);

            if (selectedSymbols.isEmpty()) {
                int selectColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
                Component symbolHint = Component.translatable("gui.modgov.pamphlet.click_for_symbols");
                int hintWidth = this.font.width(symbolHint);
                int hintX = 192 - hintWidth / 2; // Centered
                guiGraphics.drawString(this.font, symbolHint, hintX, 25, selectColor, false);
            }
        } else {
            int contentTextColor = menu.hasColor() ? menu.getTextColor() : 0x2C2C2C;

            String title = menu.getTitle();
            String content = menu.getContent();

            if (!title.isEmpty()) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(1.5f, 1.5f, 1.5f);

                Component titleComponent = Component.literal(title);
                int titleWidth = (int)(this.font.width(titleComponent) * 1.5f);
                int centeredX = (int)((384 - titleWidth) / 2 / 1.5f);

                guiGraphics.drawString(this.font, titleComponent, centeredX, (int)(55 / 1.5f), contentTextColor, false);
                guiGraphics.pose().popPose();
            }

            if (!content.isEmpty()) {
                List<FormattedCharSequence> lines = this.font.split(Component.literal(content), 320);
                int contentY = 95;

                for (int i = 0; i < Math.min(lines.size(), 5); i++) {
                    guiGraphics.drawString(this.font, lines.get(i), 32, contentY + i * 12, contentTextColor, false);
                }
            }
        }

        if (menu.isSigned()) {
            String author = menu.getAuthor();
            if (!author.isEmpty()) {
                Component authorText = Component.translatable("gui.modgov.pamphlet.signed_by", author);
                int authorColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
                guiGraphics.drawString(this.font, authorText, 32, 165, authorColor, false);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && menu.canEdit()) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            if (symbolSelectorOpen) {
                int gridStartX = x + 142;
                int gridStartY = y - 105;

                for (int i = 0; i < 25; i++) {
                    int gridX = i % 5;
                    int gridY = i / 5;
                    int symbolX = gridStartX + (gridX * 20);
                    int symbolY = gridStartY + (gridY * 20);

                    if (mouseX >= symbolX && mouseX < symbolX + 18 && mouseY >= symbolY && mouseY < symbolY + 18) {
                        toggleSymbol(i + 1);
                        return true;
                    }
                }

                // Click outside selector closes it
                symbolSelectorOpen = false;
                return true;
            }

            int symbolAreaX = x + 42;
            int symbolAreaY = y + 20;
            int symbolAreaWidth = 300;
            int symbolAreaHeight = 30;

            if (mouseX >= symbolAreaX && mouseX < symbolAreaX + symbolAreaWidth &&
                    mouseY >= symbolAreaY && mouseY < symbolAreaY + symbolAreaHeight) {
                symbolSelectorOpen = true;
                return true;
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void toggleSymbol(int symbolId) {
        if (symbolId < 1 || symbolId > 25) {
            return;
        }
        if (selectedSymbols.contains(symbolId)) {
            selectedSymbols.remove(Integer.valueOf(symbolId));
        } else if (selectedSymbols.size() < 4) {
            selectedSymbols.add(symbolId);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Close symbol selector on ESC
        if (keyCode == 256 && symbolSelectorOpen) { // ESC
            symbolSelectorOpen = false;
            return true;
        }

        if (menu.canEdit()) {
            if (titleField != null && titleField.isFocused()) {
                if (titleField.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }

                if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                    return true;
                }

                if (keyCode == 256) {
                    titleField.setFocused(false);
                    return true;
                }
            }
            if (contentField != null && contentField.isFocused()) {
                if (contentField.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }

                if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                    return true;
                }

                if (keyCode == 256) {
                    contentField.setFocused(false);
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (menu.canEdit()) {
            if (titleField != null && titleField.isFocused()) {
                return titleField.charTyped(codePoint, modifiers);
            }
            if (contentField != null && contentField.isFocused()) {
                return contentField.charTyped(codePoint, modifiers);
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if (menu.canEdit()) {
            if (titleField != null && titleField.isFocused()) {
                return false;
            }
            if (contentField != null && contentField.isFocused()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (titleField != null) titleField.tick();
        if (contentField != null) contentField.tick();
    }

    private void save() {
        if (menu.canEdit() && titleField != null && contentField != null) {
            String title = titleField.getValue();
            String content = contentField.getValue();

            UpdatePamphletPacket packet = new UpdatePamphletPacket(menu.getHand(), title, content, selectedSymbols);
            ModMessages.sendToServer(packet);

            this.onClose();
        }
    }

    private void sign() {
        if (menu.canEdit()) {
            save();

            SignPamphletPacket packet = new SignPamphletPacket(menu.getHand());
            ModMessages.sendToServer(packet);

            this.onClose();
        }
    }
}