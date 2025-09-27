package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.data.PlayerDataManager;
import net.memeland.minecraftgov.networking.ModMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdatePlayerDataPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private final UUID playerUuid;
    private final String nationality;
    private final int politicalSymbol;

    public UpdatePlayerDataPacket(UUID playerUuid, String nationality, int politicalSymbol) {
        this.playerUuid = playerUuid;
        this.nationality = nationality;
        this.politicalSymbol = politicalSymbol;
    }

    public UpdatePlayerDataPacket(FriendlyByteBuf buf) {
        try {
            this.playerUuid = buf.readUUID();
            this.nationality = buf.readUtf(32767);
            this.politicalSymbol = buf.readInt();
        } catch (Exception e) {
            LOGGER.error("Error in UpdatePlayerDataPacket.fromBytes(): " + e.getMessage());
            throw e;
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUuid);
        buf.writeUtf(nationality, 32767);
        buf.writeInt(politicalSymbol);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            try {
                ServerPlayer sender = context.getSender();

                // Rate limiting check
                if (sender != null && ModMessages.isOnCooldown(sender.getUUID(), this.getClass())) {
                    return;
                }

                if (sender != null && sender.level() instanceof ServerLevel serverLevel) {

                    LOGGER.info("Received player data update from {} for player UUID {}",
                            sender.getGameProfile().getName(), playerUuid);

                    // Security check: player can only update their own data
                    if (!sender.getUUID().equals(playerUuid)) {
                        LOGGER.warn("Player {} tried to update data for different player {}",
                                sender.getGameProfile().getName(), playerUuid);
                        sender.sendSystemMessage(Component.translatable("player_data.modgov.update.unauthorized").withStyle(ChatFormatting.RED));
                        return;
                    }

                    // Validate input data
                    String validationResult = validatePlayerData(nationality, politicalSymbol);

                    if (validationResult.equals("SUCCESS")) {
                        PlayerDataManager manager = PlayerDataManager.get(serverLevel);
                        manager.updatePlayerData(playerUuid, nationality.trim(), politicalSymbol);

                        LOGGER.info("Player data updated for {}: nationality='{}', symbol={}",
                                sender.getGameProfile().getName(), nationality.trim(), politicalSymbol);

                        sender.sendSystemMessage(Component.translatable("player_data.modgov.update.success").withStyle(ChatFormatting.GREEN));
                    } else {
                        LOGGER.info("Player data update failed: {} - Player {}",
                                validationResult, sender.getGameProfile().getName());
                        sender.sendSystemMessage(Component.translatable(validationResult).withStyle(ChatFormatting.RED));
                    }
                } else {
                    LOGGER.error("Invalid player or level in UpdatePlayerDataPacket handler");
                }
            } catch (Exception e) {
                LOGGER.error("Exception in UpdatePlayerDataPacket handler: " + e.getMessage());
                e.printStackTrace();
            }
        });
        return true;
    }

    private String validatePlayerData(String nationality, int politicalSymbol) {
        // Validate nationality length
        if (nationality.length() > 30) {
            return "player_data.modgov.update.nationality_too_long";
        }

        // Validate political symbol range
        if (politicalSymbol < 1 || politicalSymbol > 25) {
            return "player_data.modgov.update.invalid_symbol";
        }

        return "SUCCESS";
    }
}