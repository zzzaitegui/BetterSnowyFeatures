package net.memeland.minecraftgov.screen.renderer;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.RegisterPartyPacket;
import net.memeland.minecraftgov.screen.BulletinBoardMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;

public class BulletinBoardScreen extends AbstractContainerScreen<BulletinBoardMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/gui/bulletin_board_gui.png");

    private static final ResourceLocation[] SYMBOL_TEXTURES = new ResourceLocation[25];

    static {
        for (int i = 0; i < 25; i++) {
            SYMBOL_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/misc/symbols/symbol" + (i + 1) + ".png");
        }
    }

    private EditBox partyNameField;
    private Button registerButton;
    private int selectedSymbol = 1;
    private DyeColor selectedColor = DyeColor.WHITE;

    public BulletinBoardScreen(BulletinBoardMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        partyNameField = new EditBox(this.font, x + 20, y + 40, 140, 20, Component.translatable("gui.modgov.party_name"));
        partyNameField.setMaxLength(25);
        this.addRenderableWidget(partyNameField);

        registerButton = Button.builder(
                Component.translatable("gui.modgov.register_party"),
                button -> registerParty()
        ).bounds(x + 20, y + 200, 100, 20).build();
        this.addRenderableWidget(registerButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Render symbol selection grid
        for (int i = 0; i < 25; i++) {
            int gridX = i % 5;
            int gridY = i / 5;
            int symbolX = x + 130 + (gridX * 16);
            int symbolY = y + 90 + (gridY * 16);

            // Highlight selected symbol
            if (selectedSymbol == i + 1) {
                guiGraphics.fill(symbolX - 1, symbolY - 1, symbolX + 15, symbolY + 15, 0xFFFFFFFF);
            }

            // Render gray background
            guiGraphics.fill(symbolX, symbolY, symbolX + 14, symbolY + 14, 0xFF888888);

            // Render actual symbol texture, properly scaled full 16x16 to fit in 12x12 space
            int symbolSize = 12;
            int symbolCenterX = symbolX + 1; // Center the 12x12 symbol in 14x14 square
            int symbolCenterY = symbolY + 1;

            guiGraphics.blit(SYMBOL_TEXTURES[i], symbolCenterX, symbolCenterY, symbolSize, symbolSize, 0.0f, 0.0f, 16, 16, 16, 16);
        }

        // Render color selection
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < colors.length; i++) {
            int gridX = i % 4;
            int gridY = i / 4;
            int colorX = x + 20 + (gridX * 16);
            int colorY = y + 90 + (gridY * 16);

            // Highlight selected color
            if (selectedColor == colors[i]) {
                guiGraphics.fill(colorX - 1, colorY - 1, colorX + 15, colorY + 15, 0xFFFFFFFF);
            }

            // Render color square
            int colorValue = colors[i].getFireworkColor() | 0xFF000000;
            guiGraphics.fill(colorX, colorY, colorX + 14, colorY + 14, colorValue);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Draw the title
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);

        // Draw custom labels
        guiGraphics.drawString(this.font, Component.translatable("gui.modgov.party_name"), 20, 30, 0x404040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.modgov.select_color"), 20, 80, 0x404040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.modgov.select_symbol"), 130, 80, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // If text field is focused, consume ALL input to prevent GUI closing
        if (partyNameField.isFocused()) {
            if (partyNameField.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }

            if (keyCode == 256) {
                partyNameField.setFocused(false);
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (partyNameField.isFocused()) {
            return partyNameField.charTyped(codePoint, modifiers);
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !partyNameField.isFocused();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            for (int i = 0; i < 25; i++) {
                int gridX = i % 5;
                int gridY = i / 5;
                int symbolX = x + 130 + (gridX * 16);
                int symbolY = y + 90 + (gridY * 16);

                if (mouseX >= symbolX && mouseX < symbolX + 14 && mouseY >= symbolY && mouseY < symbolY + 14) {
                    selectedSymbol = i + 1;
                    return true;
                }
            }

            // Check color selection
            DyeColor[] colors = DyeColor.values();
            for (int i = 0; i < colors.length; i++) {
                int gridX = i % 4;
                int gridY = i / 4;
                int colorX = x + 20 + (gridX * 16);
                int colorY = y + 90 + (gridY * 16);

                if (mouseX >= colorX && mouseX < colorX + 14 && mouseY >= colorY && mouseY < colorY + 14) {
                    selectedColor = colors[i];
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void registerParty() {
        String partyName = partyNameField.getValue().trim();
        if (partyName != null && !partyName.isEmpty() && partyName.length() <= 25) {
            RegisterPartyPacket packet = new RegisterPartyPacket(
                    menu.getBlockPos(),
                    partyName,
                    selectedSymbol,
                    selectedColor
            );
            ModMessages.sendToServer(packet);

            partyNameField.setValue("");
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}