package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.screen.renderer.BallotBoxScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VoteDataSyncPacket {
    private final BlockPos pos;
    private final List<PartyData> availableParties;
    private final Map<String, Integer> voteData;
    private final boolean hasPlayerVoted;
    private final int totalVotes;

    public VoteDataSyncPacket(BlockPos pos, List<PartyData> availableParties, Map<String, Integer> voteData, boolean hasPlayerVoted, int totalVotes) {
        this.pos = pos;
        this.availableParties = availableParties;
        this.voteData = voteData;
        this.hasPlayerVoted = hasPlayerVoted;
        this.totalVotes = totalVotes;
    }

    public VoteDataSyncPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();

        int partyCount = buf.readInt();
        this.availableParties = new ArrayList<>();
        for (int i = 0; i < partyCount; i++) {
            String name = buf.readUtf();
            int symbolId = buf.readInt();
            DyeColor color = DyeColor.byId(buf.readInt());
            String representative = buf.readUtf();
            this.availableParties.add(new PartyData(name, symbolId, color, representative));
        }

        int voteCount = buf.readInt();
        this.voteData = new HashMap<>();
        for (int i = 0; i < voteCount; i++) {
            String partyName = buf.readUtf();
            int votes = buf.readInt();
            this.voteData.put(partyName, votes);
        }

        this.hasPlayerVoted = buf.readBoolean();
        this.totalVotes = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);

        buf.writeInt(availableParties.size());
        for (PartyData party : availableParties) {
            buf.writeUtf(party.getName());
            buf.writeInt(party.getSymbolId());
            buf.writeInt(party.getColor().getId());
            buf.writeUtf(party.getRepresentative());
        }

        buf.writeInt(voteData.size());
        for (Map.Entry<String, Integer> entry : voteData.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeInt(entry.getValue());
        }

        buf.writeBoolean(hasPlayerVoted);
        buf.writeInt(totalVotes);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            // Store the data temporarily for when screen opens
            VoteDataHolder.setVoteData(pos, availableParties, voteData, hasPlayerVoted, totalVotes);

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen instanceof BallotBoxScreen ballotBoxScreen) {
                ballotBoxScreen.receiveVoteData(availableParties, voteData, hasPlayerVoted, totalVotes);
            }
        });
        return true;
    }
}