package net.memeland.minecraftgov.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class VoteManager extends SavedData {
    private static final String DATA_NAME = "modgov_votes";

    private final Set<UUID> votedPlayers = new HashSet<>();
    private final Map<String, Integer> partyVotes = new HashMap<>();
    private final Map<String, PartyData> availableParties = new HashMap<>();
    private boolean votingStarted = false;
    private boolean votingPaused = false;

    public VoteManager() {
        super();
    }

    public static VoteManager get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(VoteManager::load, VoteManager::new, DATA_NAME);
    }

    public static VoteManager load(CompoundTag nbt) {
        VoteManager manager = new VoteManager();

        ListTag votedList = nbt.getList("votedPlayers", Tag.TAG_STRING);
        for (int i = 0; i < votedList.size(); i++) {
            manager.votedPlayers.add(UUID.fromString(votedList.getString(i)));
        }

        CompoundTag votesTag = nbt.getCompound("partyVotes");
        for (String partyName : votesTag.getAllKeys()) {
            manager.partyVotes.put(partyName, votesTag.getInt(partyName));
        }

        ListTag partiesList = nbt.getList("availableParties", Tag.TAG_COMPOUND);
        for (int i = 0; i < partiesList.size(); i++) {
            CompoundTag partyTag = partiesList.getCompound(i);
            PartyData party = PartyData.fromNBT(partyTag);
            manager.availableParties.put(party.getName(), party);
        }

        manager.votingStarted = nbt.getBoolean("votingStarted");
        manager.votingPaused = nbt.getBoolean("votingPaused");

        return manager;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag votedList = new ListTag();
        for (UUID uuid : votedPlayers) {
            votedList.add(net.minecraft.nbt.StringTag.valueOf(uuid.toString()));
        }
        nbt.put("votedPlayers", votedList);

        CompoundTag votesTag = new CompoundTag();
        for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
            votesTag.putInt(entry.getKey(), entry.getValue());
        }
        nbt.put("partyVotes", votesTag);

        ListTag partiesList = new ListTag();
        for (PartyData party : availableParties.values()) {
            partiesList.add(party.toNBT());
        }
        nbt.put("availableParties", partiesList);

        nbt.putBoolean("votingStarted", votingStarted);
        nbt.putBoolean("votingPaused", votingPaused);

        return nbt;
    }

    public void initializePartiesIfNeeded(ServerLevel level) {
        if (!votingStarted) {
            PartyManager partyManager = PartyManager.get(level);
            availableParties.clear();

            for (PartyData party : partyManager.getAllParties()) {
                availableParties.put(party.getName(), party);
                partyVotes.putIfAbsent(party.getName(), 0);
            }
            setDirty();
        }
    }

    public boolean castVote(UUID playerUuid, String partyName) {
        if (votedPlayers.contains(playerUuid)) {
            return false;
        }

        if (!availableParties.containsKey(partyName)) {
            return false;
        }

        if (!votingStarted) {
            votingStarted = true;
        }

        votedPlayers.add(playerUuid);
        partyVotes.put(partyName, partyVotes.getOrDefault(partyName, 0) + 1);

        setDirty();
        return true;
    }

    public boolean hasVoted(UUID playerUuid) {
        return votedPlayers.contains(playerUuid);
    }

    public Collection<PartyData> getAvailableParties() {
        return availableParties.values();
    }

    public int getVoteCount(String partyName) {
        return partyVotes.getOrDefault(partyName, 0);
    }

    public int getTotalVotes() {
        return votedPlayers.size();
    }

    public double getVotePercentage(String partyName) {
        int totalVotes = getTotalVotes();
        if (totalVotes == 0) {
            return 0.0;
        }
        return (double) getVoteCount(partyName) / totalVotes * 100.0;
    }

    public Map<String, Integer> getAllVotes() {
        return new HashMap<>(partyVotes);
    }

    public void clearAllVotes() {
        votedPlayers.clear();
        partyVotes.clear();
        availableParties.clear();
        votingStarted = false;
        votingPaused = false;
        setDirty();
    }

    public boolean hasVotingStarted() {
        return votingStarted;
    }

    public boolean isVotingPaused() {
        return votingPaused;
    }

    public void setVotingPaused(boolean paused) {
        this.votingPaused = paused;
        setDirty();
    }

    public boolean removePartyVotes(String partyName) {
        boolean removed = false;

        if (availableParties.remove(partyName) != null) {
            removed = true;
        }

        if (partyVotes.remove(partyName) != null) {
            removed = true;
        }

        if (removed) {
            setDirty();
        }

        return removed;
    }
}