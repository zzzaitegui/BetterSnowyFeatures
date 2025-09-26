package net.memeland.minecraftgov.item.custom;

import net.memeland.minecraftgov.data.PlayerDataManager;
import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.PlayerDataSyncPacket;
import net.memeland.minecraftgov.screen.IdCardMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class IdCardItem extends Item implements MenuProvider {
    public static final Component TITLE = Component.translatable("container.modgov.id_card");

    public IdCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        CompoundTag nbt = itemStack.getTag();
        if (nbt != null && nbt.hasUUID("owner")) {
            String ownerName = nbt.getString("ownerName");
            if (!ownerName.isEmpty()) {
                tooltip.add(Component.translatable("item.modgov.id_card.owner", ownerName).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!itemStack.is(ModItems.ID_CARD.get())) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (!level.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
                // Extract owner information from item NBT
                UUID cardOwner;
                String ownerName;

                CompoundTag nbt = itemStack.getTag();
                if (nbt != null && nbt.hasUUID("owner")) {
                    // New ID card with ownership data
                    cardOwner = nbt.getUUID("owner");
                    ownerName = nbt.getString("ownerName");
                } else {
                    // Legacy ID card or missing data - assume it belongs to the user
                    cardOwner = player.getUUID();
                    ownerName = player.getGameProfile().getName();

                    // Update the item with ownership data for future use
                    CompoundTag newNbt = itemStack.getOrCreateTag();
                    newNbt.putUUID("owner", cardOwner);
                    newNbt.putString("ownerName", ownerName);
                }

                // Send owner's player data to client before opening GUI
                PlayerDataManager manager = PlayerDataManager.get(serverLevel);
                var playerData = manager.getPlayerData(cardOwner);

                PlayerDataSyncPacket packet = new PlayerDataSyncPacket(
                        cardOwner,
                        playerData.getNationality(),
                        playerData.getPoliticalSymbol()
                );
                ModMessages.sendToPlayer(packet, serverPlayer);

                // Open screen with both owner and opener UUID data
                NetworkHooks.openScreen(serverPlayer, this, buf -> {
                    buf.writeUUID(cardOwner);        // Owner of the card
                    buf.writeUUID(player.getUUID()); // Player opening the card
                    buf.writeUtf(ownerName);         // Owner's name for display
                });
            }
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new IdCardMenu(id, inventory, player.getUUID(), player.getUUID(), player.getGameProfile().getName());
    }
}