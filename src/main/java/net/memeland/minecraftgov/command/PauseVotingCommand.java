package net.memeland.minecraftgov.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.memeland.minecraftgov.data.VoteManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PauseVotingCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pausevoting")
                .requires(source -> source.hasPermission(2)) // Require operator permission
                .executes(PauseVotingCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getLevel() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) source.getLevel();

            VoteManager voteManager = VoteManager.get(serverLevel);

            // Toggle the pause state
            boolean newPauseState = !voteManager.isVotingPaused();
            voteManager.setVotingPaused(newPauseState);

            // Log the action for server administration
            String adminName = source.getEntity() instanceof ServerPlayer player ?
                    player.getGameProfile().getName() : "Console";
            LOGGER.info("Voting {} by {}", newPauseState ? "paused" : "unpaused", adminName);

            // Send appropriate message
            if (newPauseState) {
                source.sendSuccess(Component.translatable("commands.modgov.pausevoting.paused"), true);
            } else {
                source.sendSuccess(Component.translatable("commands.modgov.pausevoting.unpaused"), true);
            }

            return 1;
        }

        source.sendFailure(Component.translatable("commands.modgov.pausevoting.failed"));
        return 0;
    }
}