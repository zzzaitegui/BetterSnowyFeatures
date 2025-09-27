package net.memeland.minecraftgov.command;

import com.mojang.brigadier.CommandDispatcher;
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

public class ResetElectionsCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resetelections")
                .requires(source -> source.hasPermission(2)) // Require operator permission
                .executes(ResetElectionsCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getLevel() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) source.getLevel();

            // Clear all parties
            PartyManager partyManager = PartyManager.get(serverLevel);
            partyManager.clearAllParties();

            // Clear all votes and reset voting state
            VoteManager voteManager = VoteManager.get(serverLevel);
            voteManager.clearAllVotes();

            // Close any open ballot box GUIs to prevent stale data
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                if (player.containerMenu instanceof BallotBoxMenu) {
                    player.closeContainer();
                }
            });

            String adminName = source.getEntity() instanceof ServerPlayer player ?
                    player.getGameProfile().getName() : "Console";
            LOGGER.info("Elections reset by {}", adminName);

            source.sendSuccess(() -> Component.translatable("commands.modgov.resetelections.success"), true);
            return 1;
        }

        source.sendFailure(Component.translatable("commands.modgov.resetelections.failed"));
        return 0;
    }
}