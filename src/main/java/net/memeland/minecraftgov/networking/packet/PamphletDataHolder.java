package net.memeland.minecraftgov.networking.packet;

import net.minecraft.world.InteractionHand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Client-side temporary storage for pamphlet data until the screen is ready
 */
public class PamphletDataHolder {
    private static final Map<String, PamphletDataInfo> storedData = new HashMap<>();

    private static final long CLEANUP_INTERVAL = 300000; // 5 minutes
    private static long lastCleanup = 0;

    public static void setPamphletData(UUID playerUuid, InteractionHand hand, String title, String content, List<Integer> symbols, boolean signed, String author) {
        String key = getKey(playerUuid, hand);
        storedData.put(key, new PamphletDataInfo(title, content, symbols, signed, author));
    }

    public static PamphletDataInfo getPamphletData(UUID playerUuid, InteractionHand hand) {
        String key = getKey(playerUuid, hand);
        return storedData.get(key);
    }

    public static void clearPamphletData(UUID playerUuid, InteractionHand hand) {
        String key = getKey(playerUuid, hand);
        storedData.remove(key);
    }

    private static String getKey(UUID playerUuid, InteractionHand hand) {
        return playerUuid.toString() + "_" + hand.name();
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

    public static class PamphletDataInfo {
        public final String title;
        public final String content;
        public final List<Integer> symbols;
        public final boolean signed;
        public final String author;

        public PamphletDataInfo(String title, String content, List<Integer> symbols, boolean signed, String author) {
            this.title = title;
            this.content = content;
            this.symbols = symbols;
            this.signed = signed;
            this.author = author;
        }
    }
}