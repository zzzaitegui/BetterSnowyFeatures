package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.screen.renderer.IdCardScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerDataSyncPacket {
    private final UUID playerUuid;
    private final String nationality;
    private final int politicalSymbol;

    public PlayerDataSyncPacket(UUID playerUuid, String nationality, int politicalSymbol) {
        this.playerUuid = playerUuid;
        this.nationality = nationality;
        this.politicalSymbol = politicalSymbol;
    }

    public PlayerDataSyncPacket(FriendlyByteBuf buf) {
        this.playerUuid = buf.readUUID();
        this.nationality = buf.readUtf();
        this.politicalSymbol = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUuid);
        buf.writeUtf(nationality);
        buf.writeInt(politicalSymbol);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Store the data temporarily for when screen opens
            PlayerDataHolder.setPlayerData(playerUuid, nationality, politicalSymbol);

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen instanceof IdCardScreen idCardScreen) {
                idCardScreen.receivePlayerData(nationality, politicalSymbol);
            }
        });
        return true;
    }
}