package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.data.PlayerData;
import net.memeland.minecraftgov.data.PlayerDataManager;
import net.memeland.minecraftgov.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class IdCardMenu extends AbstractContainerMenu {
    private final UUID cardOwnerUuid;    // Who owns this ID card
    private final UUID openerUuid;       // Who is currently viewing the card
    private final String ownerName;      // Owner's display name
    private final boolean canEdit;       // Whether the opener can edit this card

    public IdCardMenu(int id, Inventory inventory, UUID cardOwnerUuid, UUID openerUuid, String ownerName) {
        super(ModMenuTypes.ID_CARD_MENU.get(), id);
        this.cardOwnerUuid = cardOwnerUuid;
        this.openerUuid = openerUuid;
        this.ownerName = ownerName;
        this.canEdit = cardOwnerUuid.equals(openerUuid); // Only owner can edit
    }

    public IdCardMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory,
                extraData.readUUID(),    // Card owner UUID
                extraData.readUUID(),    // Opener UUID
                extraData.readUtf());    // Owner name
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        // Player must have an ID card in their inventory to keep the menu open
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.ID_CARD.get())) {
                return true;
            }
        }
        return false;
    }

    public UUID getCardOwnerUuid() {
        return cardOwnerUuid;
    }

    public UUID getOpenerUuid() {
        return openerUuid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean canEdit() {
        return canEdit;
    }

    // Get the card owner's data (not the opener's data)
    public PlayerData getPlayerData(Player player) {
        if (player.level instanceof ServerLevel serverLevel) {
            PlayerDataManager manager = PlayerDataManager.get(serverLevel);
            return manager.getPlayerData(cardOwnerUuid); // Always get owner's data
        }
        return new PlayerData();
    }

    // Legacy method for compatibility - now returns card owner UUID
    @Deprecated
    public UUID getPlayerUuid() {
        return cardOwnerUuid;
    }
}