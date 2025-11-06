package net.memeland.bsf.util;

import net.memeland.bsf.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;

public class BiomeTemperatureHelper {

    private static final Long2BooleanMap chunkCache = new Long2BooleanOpenHashMap();
    private static final Long2BooleanMap snowBlockChunkCache = new Long2BooleanOpenHashMap();
    private static final Object cacheLock = new Object();

    private static final int MAX_CACHE_SIZE = 2048;

    public static boolean isColdAt(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return false;
        }

        long chunkKey = getChunkKey(pos);

        synchronized (cacheLock) {
            if (chunkCache.containsKey(chunkKey)) {
                return chunkCache.get(chunkKey);
            }
        }

        Holder<Biome> biomeHolder = level.getBiome(pos);
        ResourceLocation biomeId = getBiomeId(biomeHolder);

        if (isInList(biomeId, ModConfigs.SNOWY_BIOMES_BLACKLIST.get())) {
            synchronized (cacheLock) {
                cacheResult(chunkKey, false);
            }
            return false;
        }

        if (isInList(biomeId, ModConfigs.SNOWY_BIOMES.get())) {
            synchronized (cacheLock) {
                cacheResult(chunkKey, true);
            }
            return true;
        }

        boolean isCold = false;
        if (ModConfigs.USE_TEMPERATURE_FALLBACK.get()) {
            Biome biome = biomeHolder.value();
            isCold = biome.coldEnoughToSnow(pos) &&
                    biome.getPrecipitationAt(pos) == Biome.Precipitation.SNOW;
        }

        synchronized (cacheLock) {
            cacheResult(chunkKey, isCold);
        }
        return isCold;
    }

    public static boolean isSnowBlockBiome(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return false;
        }

        long chunkKey = getChunkKey(pos);

        synchronized (cacheLock) {
            if (snowBlockChunkCache.containsKey(chunkKey)) {
                return snowBlockChunkCache.get(chunkKey);
            }
        }

        Holder<Biome> biomeHolder = level.getBiome(pos);
        ResourceLocation biomeId = getBiomeId(biomeHolder);

        boolean isSnowBlock = isInList(biomeId, ModConfigs.SNOW_BLOCK_BIOMES.get());

        synchronized (cacheLock) {
            cacheSnowBlockResult(chunkKey, isSnowBlock);
        }
        return isSnowBlock;
    }

    public static float getTemperatureAt(LevelReader level, BlockPos pos) {
        if (level == null || pos == null) {
            return 1.0f;
        }

        Biome biome = level.getBiome(pos).value();
        return biome.getBaseTemperature();
    }

    private static ResourceLocation getBiomeId(Holder<Biome> biomeHolder) {
        return biomeHolder.unwrapKey()
                .map(key -> key.location())
                .orElse(ResourceLocation.fromNamespaceAndPath("minecraft", "plains"));
    }

    private static boolean isInList(ResourceLocation biomeId, java.util.List<? extends String> list) {
        String biomeIdStr = biomeId.toString();
        return list.stream().anyMatch(entry -> entry.equalsIgnoreCase(biomeIdStr));
    }

    private static long getChunkKey(BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        return ((long) chunkX & 0xFFFFFFFFL) << 32 | ((long) chunkZ & 0xFFFFFFFFL);
    }

    private static void cacheResult(long chunkKey, boolean isCold) {
        if (chunkCache.size() >= MAX_CACHE_SIZE) {
            chunkCache.clear();
        }

        chunkCache.put(chunkKey, isCold);
    }

    private static void cacheSnowBlockResult(long chunkKey, boolean isSnowBlock) {
        if (snowBlockChunkCache.size() >= MAX_CACHE_SIZE) {
            snowBlockChunkCache.clear();
        }

        snowBlockChunkCache.put(chunkKey, isSnowBlock);
    }

    public static void clearCache() {
        synchronized (cacheLock) {
            chunkCache.clear();
        }
    }

    public static void clearSnowBlockCache() {
        synchronized (cacheLock) {
            snowBlockChunkCache.clear();
        }
    }

    public static void clearCacheAt(BlockPos pos) {
        long chunkKey = getChunkKey(pos);
        synchronized (cacheLock) {
            chunkCache.remove(chunkKey);
            snowBlockChunkCache.remove(chunkKey);
        }
    }
}