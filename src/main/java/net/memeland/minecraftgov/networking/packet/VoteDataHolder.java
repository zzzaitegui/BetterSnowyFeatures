package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.data.PartyData;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client-side temporary storage for vote data until the screen is ready
 */
public class VoteDataHolder {
    private static final Map<BlockPos, VoteData> storedData = new HashMap<>();

    private static final long CLEANUP_INTERVAL = 300000; // 5 minutes
    private static long lastCleanup = 0;

    public static void setVoteData(BlockPos pos, List<PartyData> parties, Map<String, Integer> votes, boolean hasVoted, int totalVotes) {
        storedData.put(pos, new VoteData(parties, votes, hasVoted, totalVotes));
    }

    public static VoteData getVoteData(BlockPos pos) {
        VoteData data = storedData.get(pos);
        return data;
    }

    public static void clearVoteData(BlockPos pos) {
        storedData.remove(pos);
    }

    /**
     * Cleanup old data periodically to prevent memory leaks
     */
    private static void cleanupOldDataIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCleanup > CLEANUP_INTERVAL) {
            // Clear all stored data older than the cleanup interval
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

    public static class VoteData {
        public final List<PartyData> parties;
        public final Map<String, Integer> votes;
        public final boolean hasVoted;
        public final int totalVotes;

        public VoteData(List<PartyData> parties, Map<String, Integer> votes, boolean hasVoted, int totalVotes) {
            this.parties = parties;
            this.votes = votes;
            this.hasVoted = hasVoted;
            this.totalVotes = totalVotes;
        }
    }
}