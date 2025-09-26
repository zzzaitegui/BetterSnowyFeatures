package net.memeland.minecraftgov.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class PlayerDataManager extends SavedData {
    private static final String DATA_NAME = "modgov_players";

    private final Map<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerDataManager() {
        super();
    }

    public static PlayerDataManager get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(PlayerDataManager::load, PlayerDataManager::new, DATA_NAME);
    }

    public static PlayerDataManager load(CompoundTag nbt) {
        PlayerDataManager manager = new PlayerDataManager();
        ListTag playersList = nbt.getList("players", Tag.TAG_COMPOUND);

        for (int i = 0; i < playersList.size(); i++) {
            CompoundTag playerTag = playersList.getCompound(i);
            UUID playerUuid = UUID.fromString(playerTag.getString("uuid"));
            PlayerData data = PlayerData.fromNBT(playerTag.getCompound("data"));
            manager.playerData.put(playerUuid, data);
        }

        return manager;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag playersList = new ListTag();

        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putString("uuid", entry.getKey().toString());
            playerTag.put("data", entry.getValue().toNBT());
            playersList.add(playerTag);
        }

        nbt.put("players", playersList);
        return nbt;
    }

    public PlayerData getPlayerData(UUID playerUuid) {
        return playerData.getOrDefault(playerUuid, new PlayerData());
    }

    public void updatePlayerData(UUID playerUuid, String nationality, int politicalSymbol) {
        PlayerData existingData = getPlayerData(playerUuid);
        // Preserve the hasReceivedIdCard flag when updating other data
        PlayerData data = new PlayerData(nationality, politicalSymbol, existingData.hasReceivedIdCard());
        playerData.put(playerUuid, data);
        setDirty();
    }

    public void updateNationality(UUID playerUuid, String nationality) {
        PlayerData data = getPlayerData(playerUuid);
        data.setNationality(nationality);
        playerData.put(playerUuid, data);
        setDirty();
    }

    public void updatePoliticalSymbol(UUID playerUuid, int politicalSymbol) {
        PlayerData data = getPlayerData(playerUuid);
        data.setPoliticalSymbol(politicalSymbol);
        playerData.put(playerUuid, data);
        setDirty();
    }

    // New methods for ID card tracking
    public boolean hasPlayerReceivedIdCard(UUID playerUuid) {
        return getPlayerData(playerUuid).hasReceivedIdCard();
    }

    public void markPlayerReceivedIdCard(UUID playerUuid) {
        PlayerData data = getPlayerData(playerUuid);
        data.setHasReceivedIdCard(true);
        playerData.put(playerUuid, data);
        setDirty();
    }

    public boolean hasPlayerData(UUID playerUuid) {
        return playerData.containsKey(playerUuid);
    }

    public void clearPlayerData(UUID playerUuid) {
        playerData.remove(playerUuid);
        setDirty();
    }

    public Collection<PlayerData> getAllPlayerData() {
        return playerData.values();
    }
}