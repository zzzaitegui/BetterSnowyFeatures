package net.memeland.minecraftgov.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class PartyManager extends SavedData {
    private static final String DATA_NAME = "modgov_parties";
    private static final int MAX_PARTIES = 8;

    private final Map<String, PartyData> parties = new HashMap<>();

    public PartyManager() {
        super();
    }

    public static PartyManager get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(PartyManager::load, PartyManager::new, DATA_NAME);
    }

    public static PartyManager load(CompoundTag nbt) {
        PartyManager manager = new PartyManager();
        ListTag partiesList = nbt.getList("parties", Tag.TAG_COMPOUND);

        for (int i = 0; i < partiesList.size(); i++) {
            CompoundTag partyTag = partiesList.getCompound(i);
            PartyData party = PartyData.fromNBT(partyTag);
            manager.parties.put(party.getName(), party);
        }

        return manager;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag partiesList = new ListTag();

        for (PartyData party : parties.values()) {
            partiesList.add(party.toNBT());
        }

        nbt.put("parties", partiesList);
        return nbt;
    }

    public boolean registerParty(PartyData party) {
        if (parties.size() >= MAX_PARTIES) {
            return false;
        }

        if (party.getName().length() > 25 || party.getName().trim().isEmpty()) {
            return false;
        }

        if (parties.containsKey(party.getName())) {
            return false;
        }

        parties.put(party.getName(), party);
        setDirty();
        return true;
    }

    public boolean removeParty(String name) {
        if (parties.remove(name) != null) {
            setDirty();
            return true;
        }
        return false;
    }

    public PartyData getParty(String name) {
        return parties.get(name);
    }

    public Collection<PartyData> getAllParties() {
        return parties.values();
    }

    public boolean hasParty(String name) {
        return parties.containsKey(name);
    }

    public void clearAllParties() {
        parties.clear();
        setDirty();
    }

    public int getPartyCount() {
        return parties.size();
    }

    public boolean canRegisterMoreParties() {
        return parties.size() < MAX_PARTIES;
    }
}