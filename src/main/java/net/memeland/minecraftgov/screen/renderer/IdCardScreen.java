package net.memeland.minecraftgov.screen.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.PlayerDataHolder;
import net.memeland.minecraftgov.networking.packet.UpdatePlayerDataPacket;
import net.memeland.minecraftgov.screen.IdCardMenu;
import net.memeland.minecraftgov.screen.widgets.CustomPaperButton;
import net.memeland.minecraftgov.screen.widgets.CustomPaperEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class IdCardScreen extends AbstractContainerScreen<IdCardMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/gui/id_card_gui.png");

    private static final ResourceLocation[] SYMBOL_TEXTURES = new ResourceLocation[25];

    static {
        for (int i = 0; i < 25; i++) {
            SYMBOL_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/misc/symbols/symbol" + (i + 1) + ".png");
        }
    }

    private EditBox nationalityField;
    private Button saveButton;
    private int selectedSymbol = 1;
    private boolean symbolSelectorOpen = false;
    private String playerName = "";
    private Player targetPlayer = null;
    private final boolean canEdit;

    public IdCardScreen(IdCardMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 384;
        this.imageHeight = 256;
        this.canEdit = menu.canEdit();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Only allow symbol selection if user can edit
        if (!canEdit) {
            return super.mouseReleased(mouseX, mouseY, button);
        }

        if (button == 0) { // Left click
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            int mainSymbolX = x + 297;
            int mainSymbolY = y + 49;
            int mainSymbolSize = 32;

            // If symbol selector is open, check for symbol selection FIRST
            if (symbolSelectorOpen) {
                // Calculate dropdown symbol positions (match renderBg calculations)
                int symbolCenterX = mainSymbolX + 16;
                int symbolCenterY = mainSymbolY + 16;
                int dropdownX = symbolCenterX - 65;
                int dropdownY = symbolCenterY - 55;
                int selectorStartX = dropdownX + 15;
                int selectorStartY = dropdownY + 5;

                for (int i = 0; i < 25; i++) {
                    int gridX = i % 5;
                    int gridY = i / 5;
                    int symbolX = selectorStartX + (gridX * 20);
                    int symbolY = selectorStartY + (gridY * 20);

                    if (mouseX >= symbolX && mouseX < symbolX + 18 && mouseY >= symbolY && mouseY < symbolY + 18) {
                        selectedSymbol = i + 1;
                        symbolSelectorOpen = false;
                        return true;
                    }
                }

                // Click outside selector area - close it
                symbolSelectorOpen = false;
                return true;
            }

            // Only check main symbol click if selector is closed
            if (mouseX >= mainSymbolX && mouseX < mainSymbolX + mainSymbolSize &&
                    mouseY >= mainSymbolY && mouseY < mainSymbolY + mainSymbolSize) {
                symbolSelectorOpen = !symbolSelectorOpen;
                return true;
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Get player info for the card owner (not the opener)
        loadPlayerInfo();

        // Only create editable fields if user can edit
        if (canEdit) {
            // Nationality input field
            nationalityField = new CustomPaperEditBox(this.font, x + 25, y + 170, 140, 20, Component.translatable("gui.modgov.nationality"));
            nationalityField.setMaxLength(30);
            this.addRenderableWidget(nationalityField);

            // Save button
            saveButton = new CustomPaperButton(x + 25, y + 205, 80, 20, Component.translatable("gui.modgov.save"), button -> savePlayerData());
            this.addRenderableWidget(saveButton);
        }

        // Load existing player data from holder
        loadPlayerDataFromHolder();
    }

    private void loadPlayerInfo() {
        UUID cardOwnerUuid = menu.getCardOwnerUuid();

        // Use the owner name from the menu (stored in item NBT)
        playerName = menu.getOwnerName();

        // Try to find the card owner by UUID in the current world for skin rendering
        targetPlayer = findPlayerByUuid(cardOwnerUuid);

        // If we can't find the player, we'll fall back to default skin in rendering
        if (targetPlayer == null) {
            // Check if the card owner is the current player
            Player currentPlayer = Minecraft.getInstance().player;
            if (currentPlayer != null && currentPlayer.getUUID().equals(cardOwnerUuid)) {
                targetPlayer = currentPlayer;
            }
        }
    }

    private void renderPlayerHeadSimple(PoseStack poseStack, int x, int y) {
        ResourceLocation skinTexture = null;
        UUID cardOwnerUuid = menu.getCardOwnerUuid();

        // If this ID card is for the current player, always use their skin
        if (Minecraft.getInstance().player != null &&
                Minecraft.getInstance().player.getUUID().equals(cardOwnerUuid)) {

            if (Minecraft.getInstance().player instanceof AbstractClientPlayer) {
                AbstractClientPlayer clientPlayer = (AbstractClientPlayer) Minecraft.getInstance().player;
                skinTexture = clientPlayer.getSkinTextureLocation();
            }
        } else {
            // For other players, try to find them in the world
            if (Minecraft.getInstance().level != null) {
                for (Player player : Minecraft.getInstance().level.players()) {
                    if (player.getUUID().equals(cardOwnerUuid) &&
                            player instanceof AbstractClientPlayer) {
                        AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
                        skinTexture = clientPlayer.getSkinTextureLocation();
                        break;
                    }
                }
            }
        }

        // If we still don't have a skin, use the default for this UUID
        if (skinTexture == null) {
            skinTexture = DefaultPlayerSkin.getDefaultSkin(cardOwnerUuid);
        }

        // Render the head
        try {
            RenderSystem.setShaderTexture(0, skinTexture);
            this.blit(poseStack, x, y, 64, 64, 8, 8, 8, 8, 64, 64);  // Face
            this.blit(poseStack, x, y, 64, 64, 40, 8, 8, 8, 64, 64); // Hat layer
        } catch (Exception e) {
            // Don't render anything if there's an error
        }
    }

    private Player findPlayerByUuid(UUID uuid) {
        if (Minecraft.getInstance().level != null) {
            for (Player player : Minecraft.getInstance().level.players()) {
                if (player.getUUID().equals(uuid)) {
                    return player;
                }
            }
        }
        return null;
    }

    private ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        try {
            // Try to get the skin from the skin manager
            ResourceLocation skin = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(gameProfile);

            // Check if it's the default skin (which indicates offline mode or no skin available)
            if (skin.equals(DefaultPlayerSkin.getDefaultSkin()) || skin.equals(DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()))) {
                return getDefaultSkinForUuid(gameProfile.getId());
            }

            return skin;
        } catch (Exception e) {
            // Fallback for any errors
            return getDefaultSkinForUuid(gameProfile.getId());
        }
    }

    private ResourceLocation getDefaultSkinForUuid(UUID uuid) {
        // Use the UUID to determine if it should be Steve or Alex skin
        // This matches Minecraft's logic for determining default skins
        return DefaultPlayerSkin.getDefaultSkin(uuid);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw main GUI texture
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight, 384, 256);

        // Render player head safely with proper error checking
        renderPlayerHeadSimple(poseStack, x + 31, y + 32);

        // Reset to main GUI texture
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Render main symbol square
        int mainSymbolX = x + 297;
        int mainSymbolY = y + 49;
        int mainSymbolSize = 32;

        // Render selected symbol in main square (32x32)
        try {
            if (SYMBOL_TEXTURES[selectedSymbol - 1] != null) {
                RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[selectedSymbol - 1]);
                blit(poseStack, mainSymbolX, mainSymbolY, mainSymbolSize, mainSymbolSize, 0, 0, 16, 16, 16, 16);
                RenderSystem.setShaderTexture(0, TEXTURE);
            }
        } catch (Exception e) {
            // Silent fallback
        }

        // Render symbol selector if open (only if can edit)
        if (symbolSelectorOpen && canEdit) {
            // Calculate dropdown position - centered over main symbol (130x110)
            int symbolCenterX = mainSymbolX + 16;
            int symbolCenterY = mainSymbolY + 16;
            int dropdownWidth = 110;
            int dropdownHeight = 110;
            int dropdownX = symbolCenterX - dropdownWidth / 2;
            int dropdownY = symbolCenterY - dropdownHeight / 2;

            fill(poseStack, dropdownX - 2, dropdownY - 2, dropdownX + dropdownWidth, dropdownY + dropdownHeight, 0xFFAC150E);
            fill(poseStack, dropdownX, dropdownY, dropdownX + dropdownWidth-2, dropdownY + dropdownHeight-2, 0xFF6C0B06);

            // Calculate symbol grid position - centered in dropdown
            int gridSize = 100; // 5x5 grid with 20px spacing = 100x100
            int selectorStartX = dropdownX + (dropdownWidth - gridSize) / 2;
            int selectorStartY = dropdownY + (dropdownHeight - gridSize) / 2;

            for (int i = 0; i < 25; i++) {
                int gridX = i % 5;
                int gridY = i / 5;
                int symbolX = selectorStartX + (gridX * 20);
                int symbolY = selectorStartY + (gridY * 20);

                fill(poseStack, symbolX, symbolY, symbolX + 18, symbolY + 18, 0XFFE7969A);

                // Render symbol texture
                try {
                    if (SYMBOL_TEXTURES[i] != null) {
                        RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[i]);
                        blit(poseStack, symbolX + 2, symbolY + 2, 14, 14, 0, 0, 16, 16, 16, 16);
                        RenderSystem.setShaderTexture(0, TEXTURE); // Reset texture
                    }
                } catch (Exception e) {
                    // Silent fallback
                }
            }
        }

        // Always reset to main GUI texture at the end
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Draw the title
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 0x9F9581);

        // Draw card owner's name (not opener's name)
        this.font.draw(poseStack, Component.literal(playerName), 25, 108, 0x9F9581);

        this.font.draw(poseStack, Component.translatable("gui.modgov.issued"), 25, 120, 0x9F9581);
        this.font.draw(poseStack, Component.translatable("gui.modgov.expires"), 25, 132, 0x9F9581);

        // Only show nationality label if there are editable fields
        if (canEdit) {
            this.font.draw(poseStack, Component.translatable("gui.modgov.nationality"), 25, 155, 0x9F9581);
        } else {
            // For read-only view, show nationality value directly
            PlayerDataHolder.PlayerDataInfo data = PlayerDataHolder.getPlayerData(menu.getCardOwnerUuid());
            String nationality = data != null ? data.nationality : "";
            if (!nationality.isEmpty()) {
                this.font.draw(poseStack, Component.translatable("gui.modgov.nationality").append(": " + nationality), 25, 155, 0x9F9581);
            }
        }

        if (!symbolSelectorOpen && canEdit) {
            this.font.draw(poseStack, Component.translatable("gui.modgov.clickselect"), 278, 18, 0x9F9581);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle text field focus only if can edit
        if (canEdit && nationalityField != null && nationalityField.isFocused()) {
            if (nationalityField.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }

            if (keyCode == 256) { // ESC key
                nationalityField.setFocus(false);
                return true;
            }
        }

        // Handle ESC key to close symbol selector (only if can edit)
        if (keyCode == 256 && symbolSelectorOpen && canEdit) { // ESC key
            symbolSelectorOpen = false;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (canEdit && nationalityField != null && nationalityField.isFocused()) {
            return nationalityField.charTyped(codePoint, modifiers);
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        // Don't close on ESC if text field is focused or symbol selector is open (when can edit)
        if (canEdit && nationalityField != null) {
            return !nationalityField.isFocused() && !symbolSelectorOpen;
        }
        return !symbolSelectorOpen;
    }

    private void savePlayerData() {
        if (!canEdit) return; // Double-check permissions

        String nationality = nationalityField.getValue().trim();

        // Send update for the card owner (not necessarily the opener)
        UpdatePlayerDataPacket packet = new UpdatePlayerDataPacket(
                menu.getCardOwnerUuid(),
                nationality,
                selectedSymbol
        );
        ModMessages.sendToServer(packet);
    }

    public void receivePlayerData(String nationality, int politicalSymbol) {
        try {
            if (canEdit && nationalityField != null) {
                nationalityField.setValue(nationality);
            }
            selectedSymbol = politicalSymbol;
        } catch (Exception e) {
            // Silent fallback
        }
    }

    private void loadPlayerDataFromHolder() {
        try {
            PlayerDataHolder.PlayerDataInfo data = PlayerDataHolder.getPlayerData(menu.getCardOwnerUuid());
            if (data != null) {
                receivePlayerData(data.nationality, data.politicalSymbol);
            } else {
                // Fallback: set default values
                receivePlayerData("", 1);
            }
        } catch (Exception e) {
            // Silent fallback
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (canEdit && nationalityField != null) {
            nationalityField.tick();
        }
    }
}