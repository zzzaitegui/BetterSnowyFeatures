package net.memeland.minecraftgov.networking.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Client-side temporary storage for player data until the screen is ready
 */
public class PlayerDataHolder {
    private static final Map<UUID, PlayerDataInfo> storedData = new HashMap<>();

    // Cleanup constants
    private static final long CLEANUP_INTERVAL = 300000; // 5 minutes
    private static long lastCleanup = 0;

    public static void setPlayerData(UUID playerUuid, String nationality, int politicalSymbol) {
        cleanupOldDataIfNeeded();
        storedData.put(playerUuid, new PlayerDataInfo(nationality, politicalSymbol));
    }

    public static PlayerDataInfo getPlayerData(UUID playerUuid) {
        cleanupOldDataIfNeeded();
        return storedData.get(playerUuid);
    }

    public static void clearPlayerData(UUID playerUuid) {
        storedData.remove(playerUuid);
    }

    /**
     * Cleanup old data periodically to prevent memory leaks
     */
    private static void cleanupOldDataIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCleanup > CLEANUP_INTERVAL) {
            // Clear all stored data older than the cleanup interval
            // Since this is temporary data for UI sync, clearing it periodically is safe
            storedData.clear();
            lastCleanup = now;
        }
    }

    /**
     * Force cleanup - useful when leaving servers or worlds
     */
    public static void forceCleanup() {
        storedData.clear();
        lastCleanup = System.currentTimeMillis();
    }

    public static class PlayerDataInfo {
        public final String nationality;
        public final int politicalSymbol;

        public PlayerDataInfo(String nationality, int politicalSymbol) {
            this.nationality = nationality;
            this.politicalSymbol = politicalSymbol;
        }
    }
}