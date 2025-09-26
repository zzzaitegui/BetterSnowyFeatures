package net.memeland.minecraftgov.event;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.data.PlayerDataManager;
import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.PamphletDataHolder;
import net.memeland.minecraftgov.networking.packet.PlayerDataHolder;
import net.memeland.minecraftgov.networking.packet.PlayerDataSyncPacket;
import net.memeland.minecraftgov.networking.packet.VoteDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Server-side events only
@Mod.EventBusSubscriber(modid = ModgovMod.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.level instanceof ServerLevel serverLevel) {
                PlayerDataManager manager = PlayerDataManager.get(serverLevel);

                // Only give ID card on first-ever join
                if (!manager.hasPlayerReceivedIdCard(player.getUUID())) {
                    giveIdCard(player);
                    // Mark that this player has now received their first ID card
                    manager.markPlayerReceivedIdCard(player.getUUID());
                }

                // Always sync player data to client when they join
                var playerData = manager.getPlayerData(player.getUUID());
                PlayerDataSyncPacket packet = new PlayerDataSyncPacket(
                        player.getUUID(),
                        playerData.getNationality(),
                        playerData.getPoliticalSymbol()
                );
                ModMessages.sendToPlayer(packet, player);
            }
        }
    }
    private static void giveIdCard(ServerPlayer player) {
        ItemStack idCard = new ItemStack(ModItems.ID_CARD.get());

        // Store ownership data in the item's NBT
        CompoundTag nbt = idCard.getOrCreateTag();
        nbt.putUUID("owner", player.getUUID());
        nbt.putString("ownerName", player.getGameProfile().getName());

        boolean added = player.getInventory().add(idCard);

        if (!added) {
            // If inventory is full, drop it
            player.drop(idCard, false);
        }
    }

    @Mod.EventBusSubscriber(modid = ModgovMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientPlayerEvents {

        @SubscribeEvent
        public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
            // Force cleanup when leaving server
            PlayerDataHolder.forceCleanup();
            PamphletDataHolder.forceCleanup();
            VoteDataHolder.forceCleanup();
        }
    }
}