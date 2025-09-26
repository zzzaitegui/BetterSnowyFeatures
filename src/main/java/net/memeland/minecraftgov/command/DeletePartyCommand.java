package net.memeland.minecraftgov.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.memeland.minecraftgov.data.PartyManager;
import net.memeland.minecraftgov.data.VoteManager;
import net.memeland.minecraftgov.screen.BallotBoxMenu;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeletePartyCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deleteparty")
                .requires(source -> source.hasPermission(2)) // Require operator permission
                .then(Commands.argument("partyname", StringArgumentType.greedyString())
                        .executes(DeletePartyCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String partyName = StringArgumentType.getString(context, "partyname").trim();

        if (source.getLevel() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) source.getLevel();

            PartyManager partyManager = PartyManager.get(serverLevel);
            VoteManager voteManager = VoteManager.get(serverLevel);

            // Check if party exists
            if (!partyManager.hasParty(partyName)) {
                source.sendFailure(Component.translatable("commands.modgov.deleteparty.not_found", partyName));
                return 0;
            }

            // Get vote count before deletion for logging
            int voteCount = voteManager.getVoteCount(partyName);

            // Remove party from PartyManager
            boolean partyRemoved = partyManager.removeParty(partyName);

            // Remove party votes from VoteManager
            boolean votesRemoved = voteManager.removePartyVotes(partyName);

            if (partyRemoved) {
                // Close any open ballot box GUIs to prevent stale data
                serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                    if (player.containerMenu instanceof BallotBoxMenu) {
                        player.closeContainer();
                    }
                });

                // Log the action for server administration
                String adminName = source.getEntity() instanceof ServerPlayer player ?
                        player.getGameProfile().getName() : "Console";
                LOGGER.info("Party '{}' deleted by {} (had {} votes)", partyName, adminName, voteCount);

                // Send success message
                source.sendSuccess(Component.translatable("commands.modgov.deleteparty.success", partyName), true);
                if (voteCount > 0) {
                    source.sendSuccess(Component.translatable("commands.modgov.deleteparty.votes_removed", voteCount), true);
                }
                return 1;
            }

            source.sendFailure(Component.translatable("commands.modgov.deleteparty.failed", partyName));
            return 0;
        }

        source.sendFailure(Component.translatable("commands.modgov.deleteparty.failed", partyName));
        return 0;
    }
}