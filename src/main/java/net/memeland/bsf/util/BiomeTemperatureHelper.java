package net.memeland.bsf.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;

public class BiomeTemperatureHelper {

    private static final Long2BooleanMap chunkCache = new Long2BooleanOpenHashMap();
    private static final Long2BooleanMap snowBlockChunkCache = new Long2BooleanOpenHashMap();

    private static final int MAX_CACHE_SIZE = 2048;

    public static boolean isColdAt(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return false;
        }

        long chunkKey = getChunkKey(pos);

        if (chunkCache.containsKey(chunkKey)) {
            return chunkCache.get(chunkKey);
        }

        Holder<Biome> biomeHolder = level.getBiome(pos);
        boolean isCold = biomeHolder.is(ModTags.Biomes.SNOWY_BIOMES);

        cacheResult(chunkKey, isCold);
        return isCold;
    }

    public static boolean isSnowBlockBiome(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return false;
        }

        Holder<Biome> biomeHolder = level.getBiome(pos);

        if (biomeHolder.is(ModTags.Biomes.SNOW_BLOCK_BIOMES)) {
            return true;
        }

        long chunkKey = getChunkKey(pos);
        if (snowBlockChunkCache.containsKey(chunkKey)) {
            return snowBlockChunkCache.get(chunkKey);
        }

        boolean isSnowBlock = false;
        cacheSnowBlockResult(chunkKey, isSnowBlock);

        return isSnowBlock;
    }

    public static float getTemperatureAt(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return 1.0f;
        }

        Biome biome = level.getBiome(pos).value();
        return biome.getBaseTemperature();
    }

    private static long getChunkKey(BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        return ((long) chunkX & 0xFFFFFFFFL) << 32 | ((long) chunkZ & 0xFFFFFFFFL);
    }

    private static void cacheResult(long chunkKey, boolean isCold) {
        if (chunkCache.size() >= MAX_CACHE_SIZE) {
            clearCache();
        }

        chunkCache.put(chunkKey, isCold);
    }

    private static void cacheSnowBlockResult(long chunkKey, boolean isSnowBlock) {
        if (snowBlockChunkCache.size() >= MAX_CACHE_SIZE) {
            clearSnowBlockCache();
        }

        snowBlockChunkCache.put(chunkKey, isSnowBlock);
    }

    public static void clearCache() {
        chunkCache.clear();
    }

    public static void clearSnowBlockCache() {
        snowBlockChunkCache.clear();
    }

    public static void clearCacheAt(BlockPos pos) {
        long chunkKey = getChunkKey(pos);
        chunkCache.remove(chunkKey);
        snowBlockChunkCache.remove(chunkKey);
    }
}