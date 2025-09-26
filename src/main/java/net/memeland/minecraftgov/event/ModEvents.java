package net.memeland.minecraftgov.event;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.command.DeletePartyCommand;
import net.memeland.minecraftgov.command.PauseVotingCommand;
import net.memeland.minecraftgov.command.ResetElectionsCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModgovMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        ResetElectionsCommand.register(event.getDispatcher());
        PauseVotingCommand.register(event.getDispatcher());
        DeletePartyCommand.register(event.getDispatcher());
    }
}
