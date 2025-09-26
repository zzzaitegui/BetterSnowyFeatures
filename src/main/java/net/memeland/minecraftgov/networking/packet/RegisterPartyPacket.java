package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.block.entity.BulletinBoardBlockEntity;
import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.data.PartyManager;
import net.memeland.minecraftgov.networking.ModMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class RegisterPartyPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private final BlockPos pos;
    private final String partyName;
    private final int symbolId;
    private final int colorId;

    public RegisterPartyPacket(BlockPos pos, String partyName, int symbolId, DyeColor color) {
        this.pos = pos;
        this.partyName = partyName;
        this.symbolId = symbolId;
        this.colorId = color.getId();
    }

    public RegisterPartyPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.partyName = buf.readUtf();
        this.symbolId = buf.readInt();
        this.colorId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(partyName);
        buf.writeInt(symbolId);
        buf.writeInt(colorId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            // Rate limiting check
            if (player != null && ModMessages.isOnCooldown(player.getUUID(), this.getClass())) {
                return;
            }

            if (player != null && player.level instanceof ServerLevel serverLevel) {

                LOGGER.info("Received party registration request from {} at position {}",
                        player.getGameProfile().getName(), pos);

                // Validate player is within reasonable range of the block
                double distance = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (distance > 64) { // 8 block range
                    LOGGER.warn("Player {} tried to register party from too far away (distance: {})",
                            player.getGameProfile().getName(), Math.sqrt(distance));
                    player.sendSystemMessage(Component.translatable("party.modgov.registration.too_far").withStyle(ChatFormatting.RED));
                    return;
                }

                // Validate block entity exists
                if (!(serverLevel.getBlockEntity(pos) instanceof BulletinBoardBlockEntity)) {
                    LOGGER.warn("Player {} tried to register party at invalid position {}",
                            player.getGameProfile().getName(), pos);
                    player.sendSystemMessage(Component.translatable("party.modgov.registration.invalid_location").withStyle(ChatFormatting.RED));
                    return;
                }

                // Get party manager and validate registration
                PartyManager manager = PartyManager.get(serverLevel);
                DyeColor color = DyeColor.byId(colorId);
                String representative = player.getGameProfile().getName();
                String trimmedName = partyName.trim();

                // Validate registration
                String validationResult = validateRegistration(manager, trimmedName);

                if (validationResult.equals("SUCCESS")) {
                    // Actually register the party
                    PartyData party = new PartyData(trimmedName, symbolId, color, representative);
                    boolean registered = manager.registerParty(party);

                    if (registered) {
                        LOGGER.info("Party '{}' registered by player {} with symbol {} and color {}",
                                trimmedName, representative, symbolId, color.getName());

                        // Send success messages
                        player.sendSystemMessage(Component.translatable("party.modgov.registration.success").withStyle(ChatFormatting.GREEN));
                        player.sendSystemMessage(Component.translatable("party.modgov.registration.success_detail", trimmedName).withStyle(ChatFormatting.GRAY));
                    } else {
                        LOGGER.error("Party registration failed unexpectedly for '{}' by {}", trimmedName, representative);
                        player.sendSystemMessage(Component.translatable("party.modgov.registration.unknown_error").withStyle(ChatFormatting.RED));
                    }
                } else {
                    LOGGER.info("Party registration failed: {} - '{}' by player {}",
                            validationResult, trimmedName, representative);
                    player.sendSystemMessage(Component.translatable(validationResult).withStyle(ChatFormatting.RED));
                }
            } else {
                LOGGER.error("Invalid player or level in RegisterPartyPacket handler");
            }
        });
        return true;
    }

    private String validateRegistration(PartyManager manager, String name) {
        // Check name validity
        if (name.isEmpty() || name.length() > 25) {
            return "party.modgov.registration.invalid_name";
        }

        // Check if party already exists
        if (manager.hasParty(name)) {
            LOGGER.info("Duplicate party check: '{}' already exists. Current parties: {}",
                    name, manager.getAllParties().stream().map(p -> p.getName()).toList());
            return "party.modgov.registration.duplicate";
        }

        // Check party limit
        if (!manager.canRegisterMoreParties()) {
            return "party.modgov.registration.too_many";
        }

        return "SUCCESS";
    }
}