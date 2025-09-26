package net.memeland.minecraftgov.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.RegisterPartyPacket;
import net.memeland.minecraftgov.screen.BulletinBoardMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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

        // Party name input field - adjusted for 256 width
        partyNameField = new EditBox(this.font, x + 20, y + 40, 140, 20, Component.translatable("gui.modgov.party_name"));
        partyNameField.setMaxLength(25);
        this.addRenderableWidget(partyNameField);

        // Register button - moved down a bit for better spacing
        registerButton = new Button(x + 20, y + 200, 100, 20, Component.translatable("gui.modgov.register_party"), button -> registerParty());
        this.addRenderableWidget(registerButton);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw your 256x256 texture
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        // Render symbol selection grid (5x5 grid for 25 symbols) - with actual textures
        for (int i = 0; i < 25; i++) {
            int gridX = i % 5;
            int gridY = i / 5;
            int symbolX = x + 130 + (gridX * 16); // Using your coordinates
            int symbolY = y + 90 + (gridY * 16);  // Using your coordinates

            // Highlight selected symbol
            if (selectedSymbol == i + 1) {
                fill(poseStack, symbolX - 1, symbolY - 1, symbolX + 15, symbolY + 15, 0xFFFFFFFF);
            }

            // Render gray background (consistent with color squares)
            fill(poseStack, symbolX, symbolY, symbolX + 14, symbolY + 14, 0xFF888888);

            // Render actual symbol texture, properly scaled
            int symbolSize = 12; // Adjust this to make symbols bigger/smaller
            int symbolCenterX = symbolX + 1; // Center the 12x12 symbol in 14x14 square
            int symbolCenterY = symbolY + 1;

            RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[i]);
            // METHOD 4: This blit method properly scales the texture!
            // Parameters: (poseStack, x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight)
            blit(poseStack, symbolCenterX, symbolCenterY, symbolSize, symbolSize, 0, 0, 16, 16, 16, 16);

            // Reset to main GUI texture for other elements
            RenderSystem.setShaderTexture(0, TEXTURE);
        }

        // Render color selection (4x4 grid for 16 colors) - adjusted positions
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < colors.length; i++) {
            int gridX = i % 4;
            int gridY = i / 4;
            int colorX = x + 20 + (gridX * 16);  // Tighter spacing
            int colorY = y + 90 + (gridY * 16);  // Adjusted position

            // Highlight selected color
            if (selectedColor == colors[i]) {
                fill(poseStack, colorX - 1, colorY - 1, colorX + 15, colorY + 15, 0xFFFFFFFF);
            }

            // Render color square
            int colorValue = colors[i].getFireworkColor() | 0xFF000000;
            fill(poseStack, colorX, colorY, colorX + 14, colorY + 14, colorValue);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Draw the title
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);

        // Draw our custom labels (adjusted for new layout)
        this.font.draw(poseStack, Component.translatable("gui.modgov.party_name"), 20, 30, 0x404040);
        this.font.draw(poseStack, Component.translatable("gui.modgov.select_color"), 20, 80, 0x404040);
        this.font.draw(poseStack, Component.translatable("gui.modgov.select_symbol"), 130, 80, 0x404040);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
        // Note: Labels are now handled in renderLabels(), not here
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // CRITICAL: If text field is focused, consume ALL input to prevent GUI closing
        if (partyNameField.isFocused()) {
            // Try to let the text field handle the key first
            if (partyNameField.keyPressed(keyCode, scanCode, modifiers)) {
                return true; // Text field consumed it
            }

            // If text field didn't consume it, but it's the inventory key, we still consume it
            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true; // Prevent inventory from opening/closing
            }

            // Also prevent ESC from closing when typing (optional)
            if (keyCode == 256) { // ESC key
                partyNameField.setFocus(false); // Just unfocus instead of closing
                return true;
            }
        }

        // Default behavior when text field is not focused
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        // If text field is focused, always let it handle character input
        if (partyNameField.isFocused()) {
            return partyNameField.charTyped(codePoint, modifiers);
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        // Don't close on ESC if text field is focused
        return !partyNameField.isFocused();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            // Check symbol selection (updated coordinates)
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

            // Check color selection (updated coordinates)
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

            // Clear the field immediately (simpler approach)
            partyNameField.setValue("");
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}