package net.memeland.minecraftgov.screen.renderer;

import com.mojang.authlib.GameProfile;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.PlayerDataHolder;
import net.memeland.minecraftgov.networking.packet.UpdatePlayerDataPacket;
import net.memeland.minecraftgov.screen.IdCardMenu;
import net.memeland.minecraftgov.screen.widgets.CustomPaperButton;
import net.memeland.minecraftgov.screen.widgets.CustomPaperEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.AbstractClientPlayer;
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
        // Only allow symbol selection if user can edit (owner)
        if (!canEdit) {
            return super.mouseReleased(mouseX, mouseY, button);
        }

        if (button == 0) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            int mainSymbolX = x + 297;
            int mainSymbolY = y + 49;
            int mainSymbolSize = 32;

            // If symbol selector is open, check for symbol selection FIRST
            if (symbolSelectorOpen) {
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

                // Click outside selector area closes it
                symbolSelectorOpen = false;
                return true;
            }

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

            saveButton = new CustomPaperButton(x + 25, y + 205, 80, 20, Component.translatable("gui.modgov.save"), button -> savePlayerData());
            this.addRenderableWidget(saveButton);
        }

        // Load existing player data from holder
        loadPlayerDataFromHolder();
    }

    private void loadPlayerInfo() {
        UUID cardOwnerUuid = menu.getCardOwnerUuid();

        playerName = menu.getOwnerName();

        targetPlayer = findPlayerByUuid(cardOwnerUuid);

        if (targetPlayer == null) {
            Player currentPlayer = Minecraft.getInstance().player;
            if (currentPlayer != null && currentPlayer.getUUID().equals(cardOwnerUuid)) {
                targetPlayer = currentPlayer;
            }
        }
    }

    private void renderPlayerHeadSimple(GuiGraphics guiGraphics, int x, int y) {
        ResourceLocation skinTexture = null;
        UUID cardOwnerUuid = menu.getCardOwnerUuid();

        if (Minecraft.getInstance().player != null &&
                Minecraft.getInstance().player.getUUID().equals(cardOwnerUuid)) {

            if (Minecraft.getInstance().player instanceof AbstractClientPlayer) {
                AbstractClientPlayer clientPlayer = (AbstractClientPlayer) Minecraft.getInstance().player;
                skinTexture = clientPlayer.getSkinTextureLocation();
            }
        } else {
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

        if (skinTexture == null) {
            skinTexture = DefaultPlayerSkin.getDefaultSkin(cardOwnerUuid);
        }

        try {
            guiGraphics.blit(skinTexture, x, y, 64, 64, 8.0f, 8.0f, 8, 8, 64, 64);  // Face
            guiGraphics.blit(skinTexture, x, y, 64, 64, 40.0f, 8.0f, 8, 8, 64, 64); // Hat layer
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
            ResourceLocation skin = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(gameProfile);

            if (skin.equals(DefaultPlayerSkin.getDefaultSkin()) || skin.equals(DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()))) {
                return getDefaultSkinForUuid(gameProfile.getId());
            }

            return skin;
        } catch (Exception e) {
            return getDefaultSkinForUuid(gameProfile.getId());
        }
    }

    private ResourceLocation getDefaultSkinForUuid(UUID uuid) {
        return DefaultPlayerSkin.getDefaultSkin(uuid);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 384, 256);

        renderPlayerHeadSimple(guiGraphics, x + 31, y + 32);

        // Render main symbol square
        int mainSymbolX = x + 297;
        int mainSymbolY = y + 49;
        int mainSymbolSize = 32;

        try {
            if (SYMBOL_TEXTURES[selectedSymbol - 1] != null) {
                guiGraphics.blit(SYMBOL_TEXTURES[selectedSymbol - 1], mainSymbolX, mainSymbolY, mainSymbolSize, mainSymbolSize, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        } catch (Exception e) {
            // Silent fallback
        }

        if (symbolSelectorOpen && canEdit) {
            int symbolCenterX = mainSymbolX + 16;
            int symbolCenterY = mainSymbolY + 16;
            int dropdownWidth = 110;
            int dropdownHeight = 110;
            int dropdownX = symbolCenterX - dropdownWidth / 2;
            int dropdownY = symbolCenterY - dropdownHeight / 2;

            guiGraphics.fill(dropdownX - 2, dropdownY - 2, dropdownX + dropdownWidth, dropdownY + dropdownHeight, 0xFFAC150E);
            guiGraphics.fill(dropdownX, dropdownY, dropdownX + dropdownWidth-2, dropdownY + dropdownHeight-2, 0xFF6C0B06);

            int gridSize = 100;
            int selectorStartX = dropdownX + (dropdownWidth - gridSize) / 2;
            int selectorStartY = dropdownY + (dropdownHeight - gridSize) / 2;

            for (int i = 0; i < 25; i++) {
                int gridX = i % 5;
                int gridY = i / 5;
                int symbolX = selectorStartX + (gridX * 20);
                int symbolY = selectorStartY + (gridY * 20);

                guiGraphics.fill(symbolX, symbolY, symbolX + 18, symbolY + 18, 0XFFE7969A);

                try {
                    if (SYMBOL_TEXTURES[i] != null) {
                        guiGraphics.blit(SYMBOL_TEXTURES[i], symbolX + 2, symbolY + 2, 14, 14, 0.0f, 0.0f, 16, 16, 16, 16);
                    }
                } catch (Exception e) {
                    // Silent fallback
                }
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x9F9581, false);

        guiGraphics.drawString(this.font, Component.literal(playerName), 25, 108, 0x9F9581, false);

        guiGraphics.drawString(this.font, Component.translatable("gui.modgov.issued"), 25, 120, 0x9F9581, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.modgov.expires"), 25, 132, 0x9F9581, false);

        if (canEdit) {
            guiGraphics.drawString(this.font, Component.translatable("gui.modgov.nationality"), 25, 155, 0x9F9581, false);
        } else {
            PlayerDataHolder.PlayerDataInfo data = PlayerDataHolder.getPlayerData(menu.getCardOwnerUuid());
            String nationality = data != null ? data.nationality : "";
            if (!nationality.isEmpty()) {
                guiGraphics.drawString(this.font, Component.translatable("gui.modgov.nationality").append(": " + nationality), 25, 155, 0x9F9581, false);
            }
        }

        if (!symbolSelectorOpen && canEdit) {
            guiGraphics.drawString(this.font, Component.translatable("gui.modgov.clickselect"), 278, 18, 0x9F9581, false);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (canEdit && nationalityField != null && nationalityField.isFocused()) {
            if (nationalityField.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }

            if (keyCode == 256) { // ESC key
                nationalityField.setFocused(false);
                return true;
            }
        }

        if (keyCode == 256 && symbolSelectorOpen && canEdit) {
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
        if (canEdit && nationalityField != null) {
            return !nationalityField.isFocused() && !symbolSelectorOpen;
        }
        return !symbolSelectorOpen;
    }

    private void savePlayerData() {
        if (!canEdit) return;

        String nationality = nationalityField.getValue().trim();

        UpdatePlayerDataPacket packet = new UpdatePlayerDataPacket(
                menu.getCardOwnerUuid(),
                nationality,
                selectedSymbol
        );
        ModMessages.sendToServer(packet);

        this.onClose();
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