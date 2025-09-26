package net.memeland.minecraftgov.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.UpdatePamphletPacket;
import net.memeland.minecraftgov.networking.packet.SignPamphletPacket;
import net.memeland.minecraftgov.screen.PamphletMenu;
import net.minecraft.client.gui.components.Button;
import net.memeland.minecraftgov.screen.widgets.SimpleMultilineEditBox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PamphletScreen extends AbstractContainerScreen<PamphletMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/gui/pamphlet_gui.png");

    // Symbol textures array
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

    // Symbol system
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
        // Load symbols from menu
        selectedSymbols.clear();
        selectedSymbols.addAll(menu.getSymbols());

        // Only create EditBox widgets if the pamphlet can be edited
        if (menu.canEdit()) {
            // Title field - adjusted down to make room for symbols
            titleField = new EditBox(this.font, x + 32, y + 50, 320, 20, Component.translatable("gui.modgov.pamphlet.title"));
            titleField.setMaxLength(41);
            titleField.setValue(menu.getTitle());
            titleField.setEditable(true);
            this.addRenderableWidget(titleField);

            // Content field - adjusted down to make room for symbols
            contentField = new SimpleMultilineEditBox(this.font, x + 32, y + 90, 320, 60, Component.translatable("gui.modgov.pamphlet.content"), 5);
            contentField.setMaxLength(500);
            contentField.setValue(menu.getContent());
            contentField.setEditable(true);
            this.addRenderableWidget(contentField);

            // Buttons for editing
            saveButton = new Button(x + 32, y + 160, 60, 20,
                    Component.translatable("gui.modgov.save"),
                    button -> save());
            this.addRenderableWidget(saveButton);

            signButton = new Button(x + 102, y + 160, 60, 20,
                    Component.translatable("gui.modgov.sign"),
                    button -> sign());
            this.addRenderableWidget(signButton);
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Apply color tinting to the background
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

        // Draw main GUI texture
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight, 384, 200);

        // Reset color to white after drawing background
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Render symbols in header (both edit and read mode)
        renderSymbolsInHeader(poseStack, x, y);

        // Render symbol selector if open (edit mode only)
        if (symbolSelectorOpen && menu.canEdit()) {
            renderSymbolSelector(poseStack, x, y, mouseX, mouseY);
        }
    }

    private void renderSymbolsInHeader(PoseStack poseStack, int x, int y) {
        if (selectedSymbols.isEmpty()) return;

        int headerY = y + 15;
        int symbolSize = selectedSymbols.size() == 1 ? 32 : 20; // Later: bigger if only 1 symbol
        int totalWidth = selectedSymbols.size() * (symbolSize + 4) - 4;
        int startX = x + (imageWidth - totalWidth) / 2;

        for (int i = 0; i < selectedSymbols.size(); i++) {
            int symbolId = selectedSymbols.get(i);
            if (symbolId >= 1 && symbolId <= 25) {
                try {
                    RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[symbolId - 1]);
                    int symbolX = startX + i * (symbolSize + 4);
                    blit(poseStack, symbolX, headerY, symbolSize, symbolSize, 0, 0, 16, 16, 16, 16);
                } catch (Exception e) {
                    // Silent fallback if texture missing
                }
            }
        }

        // Reset to main texture
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    private void renderSymbolSelector(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
        // Symbol selector area - positioned above the symbol display area
        int selectorX = x + 132; // Centered over the GUI
        int selectorY = y - 110;  // Above the GUI
        int selectorWidth = 120;
        int selectorHeight = 110;

        // Background with border
        int borderColor = menu.hasColor() ? menu.getTextColor() : 0xFF777774;
        int backgroundColor = menu.hasColor() ? menu.getBackgroundColor() : 0xFFDCDAD5;

        fill(poseStack, selectorX - 2, selectorY - 2, selectorX + selectorWidth, selectorY + selectorHeight, borderColor); // BORDER
        fill(poseStack, selectorX, selectorY, selectorX + selectorWidth - 2, selectorY + selectorHeight - 2, backgroundColor); // BACKGROUND

        // Symbol grid (5x5)
        int gridStartX = selectorX + 10;
        int gridStartY = selectorY + 5;

        for (int i = 0; i < 25; i++) {
            int gridX = i % 5;
            int gridY = i / 5;
            int symbolX = gridStartX + (gridX * 20);
            int symbolY = gridStartY + (gridY * 20);

            // Background for each symbol slot
            int bgColor = selectedSymbols.contains(i + 1) ? 0xFF90EE90 : 0xFFFFFFFF; // SYMBOL BACKGROUND
            fill(poseStack, symbolX, symbolY, symbolX + 18, symbolY + 18, bgColor);

            // Symbol texture
            try {
                RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[i]);
                blit(poseStack, symbolX + 1, symbolY + 1, 16, 16, 0, 0, 16, 16, 16, 16);
            } catch (Exception e) {
                // Silent fallback if texture missing
            }
        }

        // Reset to main texture
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Default color for UI elements

        int defaultTextColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
        this.font.draw(poseStack, this.title, 8, 9, defaultTextColor);

        if (menu.canEdit()) {
            // Edit mode: Show field labels and symbol hint
            this.font.draw(poseStack, Component.translatable("gui.modgov.pamphlet.title"), 32, 40, defaultTextColor);
            this.font.draw(poseStack, Component.translatable("gui.modgov.pamphlet.content"), 32, 80, defaultTextColor);

            // Symbol area hint (only if no symbols selected)
            if (selectedSymbols.isEmpty()) {
                int selectColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
                Component symbolHint = Component.translatable("gui.modgov.pamphlet.click_for_symbols");
                int hintWidth = this.font.width(symbolHint);
                int hintX = 192 - hintWidth / 2; // Centered
                this.font.draw(poseStack, symbolHint, hintX, 25, selectColor);
            }
        } else {
            // Read mode: Show clean text with tinted color if available
            int contentTextColor = menu.hasColor() ? menu.getTextColor() : 0x2C2C2C;

            String title = menu.getTitle();
            String content = menu.getContent();

            // Render title - larger and centered (adjusted for symbols)
            if (!title.isEmpty()) {
                poseStack.pushPose();
                poseStack.scale(1.5f, 1.5f, 1.5f);

                Component titleComponent = Component.literal(title);
                int titleWidth = (int)(this.font.width(titleComponent) * 1.5f);
                int centeredX = (int)((384 - titleWidth) / 2 / 1.5f);

                this.font.draw(poseStack, titleComponent, centeredX, (int)(55 / 1.5f), contentTextColor);
                poseStack.popPose();
            }

            // Render content - normal size, word-wrapped (adjusted for symbols)
            if (!content.isEmpty()) {
                List<FormattedCharSequence> lines = this.font.split(Component.literal(content), 320);
                int contentY = 95;

                for (int i = 0; i < Math.min(lines.size(), 5); i++) {
                    this.font.draw(poseStack, lines.get(i), 32, contentY + i * 12, contentTextColor);
                }
            }
        }

        // Show author if signed
        if (menu.isSigned()) {
            String author = menu.getAuthor();
            if (!author.isEmpty()) {
                Component authorText = Component.translatable("gui.modgov.pamphlet.signed_by", author);
                int authorColor = menu.hasColor() ? menu.getTextColor() : 0x404040;
                this.font.draw(poseStack, authorText, 32, 165, authorColor);
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && menu.canEdit()) { // Left click in edit mode only
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            // Symbol selector interaction
            if (symbolSelectorOpen) {
                int gridStartX = x + 142; // Match selector position
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

                // Click outside selector -> close it
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
        } else if (selectedSymbols.size() < 4) { // Max 4 symbols
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

        // Only handle field input if in edit mode
        if (menu.canEdit()) {
            if (titleField != null && titleField.isFocused()) {
                return titleField.keyPressed(keyCode, scanCode, modifiers);
            }
            if (contentField != null && contentField.isFocused()) {
                return contentField.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        // Only handle field input if in edit mode
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
    public void containerTick() {
        super.containerTick();
        // Only tick fields if they exist (edit mode)
        if (titleField != null) titleField.tick();
        if (contentField != null) contentField.tick();
    }

    private void save() {
        // Only save if in edit mode and fields exist
        if (menu.canEdit() && titleField != null && contentField != null) {
            String title = titleField.getValue();
            String content = contentField.getValue();

            // You'll need to update your UpdatePamphletPacket to include symbols
            // For now, this assumes the packet accepts symbols parameter
            UpdatePamphletPacket packet = new UpdatePamphletPacket(menu.getHand(), title, content, selectedSymbols);
            ModMessages.sendToServer(packet);
        }
    }

    private void sign() {
        // Only sign if in edit mode
        if (menu.canEdit()) {
            // Save first, then sign
            save();

            SignPamphletPacket packet = new SignPamphletPacket(menu.getHand());
            ModMessages.sendToServer(packet);

            this.onClose();
        }
    }
}