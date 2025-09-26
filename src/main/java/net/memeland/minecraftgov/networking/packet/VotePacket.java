package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.block.entity.BallotBoxBlockEntity;
import net.memeland.minecraftgov.data.VoteManager;
import net.memeland.minecraftgov.item.ModItems;
import net.memeland.minecraftgov.networking.ModMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class VotePacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private final BlockPos pos;
    private final String partyName;

    public VotePacket(BlockPos pos, String partyName) {
        this.pos = pos;
        this.partyName = partyName;
    }

    public VotePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.partyName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(partyName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null && ModMessages.isOnCooldown(player.getUUID(), this.getClass())) {
                return;
            }

            if (player != null && player.level instanceof ServerLevel serverLevel) {

                LOGGER.info("Received vote request from {} for party '{}' at position {}",
                        player.getGameProfile().getName(), partyName, pos);

                if (!(serverLevel.getBlockEntity(pos) instanceof BallotBoxBlockEntity ballotBox)) {
                    LOGGER.warn("Player {} tried to vote at invalid ballot box position {}",
                            player.getGameProfile().getName(), pos);
                    player.sendSystemMessage(Component.translatable("vote.modgov.invalid_location").withStyle(ChatFormatting.RED));
                    return;
                }

                double distance = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (distance > 64) {
                    player.sendSystemMessage(Component.translatable("vote.modgov.too_far").withStyle(ChatFormatting.RED));
                    return;
                }

                ItemStackHandler inventory = ballotBox.getInventory();

                ItemStack ballotStack = inventory.getStackInSlot(0);
                if (ballotStack.isEmpty() || !ballotStack.is(ModItems.BALLOT.get())) {
                    LOGGER.info("Player {} tried to vote without ballot at {}",
                            player.getGameProfile().getName(), pos);
                    player.sendSystemMessage(Component.translatable("vote.modgov.no_ballot").withStyle(ChatFormatting.RED));
                    return;
                }

                ItemStack idCardStack = inventory.getStackInSlot(1);
                if (idCardStack.isEmpty() || !idCardStack.is(ModItems.ID_CARD.get())) {
                    LOGGER.info("Player {} tried to vote without ID card at {}",
                            player.getGameProfile().getName(), pos);
                    player.sendSystemMessage(Component.translatable("vote.modgov.no_id_card").withStyle(ChatFormatting.RED));
                    return;
                }

                VoteManager voteManager = VoteManager.get(serverLevel);

                voteManager.initializePartiesIfNeeded(serverLevel);

                String validationResult = validateVote(voteManager, player, partyName);

                if (validationResult.equals("SUCCESS")) {
                    boolean voteSuccessful = voteManager.castVote(player.getUUID(), partyName);

                    if (voteSuccessful) {
                        System.out.println(player.getGameProfile().getName() + " voted for " + partyName);

                        LOGGER.info("Player {} successfully voted for party '{}'",
                                player.getGameProfile().getName(), partyName);

                        ballotStack.shrink(1);
                        inventory.setStackInSlot(0, ballotStack);

                        ejectIdCard(player, inventory, idCardStack);

                        player.sendSystemMessage(Component.translatable("vote.modgov.success").withStyle(ChatFormatting.GREEN));
                        player.sendSystemMessage(Component.translatable("vote.modgov.success_detail", partyName).withStyle(ChatFormatting.GRAY));

                        ballotBox.setChanged();
                    } else {
                        LOGGER.error("Vote casting failed unexpectedly for player {} voting for '{}'",
                                player.getGameProfile().getName(), partyName);
                        player.sendSystemMessage(Component.translatable("vote.modgov.unknown_error").withStyle(ChatFormatting.GOLD));
                    }
                } else {
                    LOGGER.info("Vote validation failed: {} - Player {} voting for '{}'",
                            validationResult, player.getGameProfile().getName(), partyName);
                    player.sendSystemMessage(Component.translatable(validationResult).withStyle(ChatFormatting.GOLD));
                }
            } else {
                LOGGER.error("Invalid player or level in VotePacket handler");
            }
        });
        return true;
    }

    private void ejectIdCard(ServerPlayer player, ItemStackHandler inventory, ItemStack idCardStack) {
        boolean added = player.getInventory().add(idCardStack.copy());

        if (added) {
            inventory.setStackInSlot(1, ItemStack.EMPTY);
            LOGGER.info("ID card ejected to {}'s inventory", player.getGameProfile().getName());
        } else {
            ItemEntity droppedItem = new ItemEntity(
                    player.level,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    idCardStack.copy()
            );
            player.level.addFreshEntity(droppedItem);

            inventory.setStackInSlot(1, ItemStack.EMPTY);

            LOGGER.info("ID card dropped in world for {} (inventory full)", player.getGameProfile().getName());
            player.sendSystemMessage(Component.translatable("vote.modgov.id_card_dropped").withStyle(ChatFormatting.YELLOW));
        }
    }

    private String validateVote(VoteManager voteManager, ServerPlayer player, String partyName) {
        if (voteManager.isVotingPaused()) {
            return "vote.modgov.voting_paused";
        }

        if (voteManager.hasVoted(player.getUUID())) {
            return "vote.modgov.already_voted";
        }

        boolean partyExists = voteManager.getAvailableParties().stream()
                .anyMatch(party -> party.getName().equals(partyName));

        if (!partyExists) {
            return "vote.modgov.party_not_found";
        }

        if (voteManager.getAvailableParties().isEmpty()) {
            return "vote.modgov.no_parties";
        }

        return "SUCCESS";
    }
}