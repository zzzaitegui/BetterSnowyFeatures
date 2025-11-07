package net.memeland.bsf.worldgen;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.ModConfigs;
import net.memeland.bsf.block.ModBlocks;
import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@EventBusSubscriber(modid = BetterSnowyFeaturesMod.MOD_ID)
public class IcicleWorldgenDecorator {

    private static final String NBT_KEY = "bsf_icicles_decorated";

    private static final float ICICLE_CHANCE = 0.2f;
    private static final int MAX_ICICLES_PER_CHUNK = 8;
    private static final int SEARCH_DEPTH = 30;
    private static final int MIN_Y = 60;

    private static final Map<ChunkPos, Boolean> chunkDecorationStatus = new HashMap<>();
    private static final Queue<ChunkPos> pendingChunks = new LinkedList<>();

    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load event) {
        if (!ModConfigs.ENABLE_ICICLES.get()) {
            return;
        }

        CompoundTag nbt = event.getData();
        ChunkPos chunkPos = event.getChunk().getPos();

        if (nbt.contains(NBT_KEY)) {
            boolean decorated = nbt.getBoolean(NBT_KEY);
            chunkDecorationStatus.put(chunkPos, decorated);
        }
    }

    @SubscribeEvent
    public static void onChunkDataSave(ChunkDataEvent.Save event) {
        if (!ModConfigs.ENABLE_ICICLES.get()) {
            return;
        }

        ChunkPos chunkPos = event.getChunk().getPos();
        Boolean decorated = chunkDecorationStatus.get(chunkPos);

        if (decorated != null && decorated) {
            CompoundTag nbt = event.getData();
            nbt.putBoolean(NBT_KEY, true);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!ModConfigs.ENABLE_ICICLES.get()) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }

        ChunkPos chunkPos = chunk.getPos();

        Boolean decorated = chunkDecorationStatus.get(chunkPos);
        if (decorated != null && decorated) {
            return;
        }

        if (!pendingChunks.contains(chunkPos)) {
            pendingChunks.add(chunkPos);
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        ChunkPos chunkPos = event.getChunk().getPos();
        chunkDecorationStatus.remove(chunkPos);
    }

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (!ModConfigs.ENABLE_ICICLES.get()) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (pendingChunks.isEmpty()) {
            return;
        }

        int processed = 0;
        while (!pendingChunks.isEmpty() && processed < 5) {
            ChunkPos chunkPos = pendingChunks.poll();
            if (chunkPos == null) {
                break;
            }

            Boolean decorated = chunkDecorationStatus.get(chunkPos);
            if (decorated != null && decorated) {
                continue;
            }

            LevelChunk chunk = serverLevel.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
            if (chunk == null) {
                continue;
            }

            BlockPos centerPos = new BlockPos(chunkPos.getMiddleBlockX(), 100, chunkPos.getMiddleBlockZ());

            if (!BiomeTemperatureHelper.isColdAt(serverLevel, centerPos)) {
                chunkDecorationStatus.put(chunkPos, true);
                chunk.setUnsaved(true);
                continue;
            }

            addIciclesToChunk(serverLevel, chunk);
            chunkDecorationStatus.put(chunkPos, true);
            chunk.setUnsaved(true);
            processed++;
        }
    }

    private static void addIciclesToChunk(ServerLevel level, LevelChunk chunk) {
        RandomSource random = level.getRandom();
        ChunkPos chunkPos = chunk.getPos();
        int iciclesPlaced = 0;

        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();

        int attempts = 64;

        for (int attempt = 0; attempt < attempts && iciclesPlaced < MAX_ICICLES_PER_CHUNK; attempt++) {
            int xOffset = random.nextInt(16);
            int zOffset = random.nextInt(16);

            int x = minX + xOffset;
            int z = minZ + zOffset;

            int surfaceY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xOffset, zOffset);

            for (int y = surfaceY; y > Math.max(surfaceY - 15, MIN_Y); y--) {
                BlockPos pos = new BlockPos(x, y, z);
                BlockState state = chunk.getBlockState(pos);

                if (state.is(BlockTags.LEAVES)) {
                    if (random.nextFloat() < ICICLE_CHANCE) {
                        BlockPos belowPos = pos.below();
                        BlockState belowState = chunk.getBlockState(belowPos);

                        if (belowState.isAir()) {
                            level.setBlock(belowPos, ModBlocks.ICICLE.get().defaultBlockState(), 3);
                            iciclesPlaced++;
                            break;
                        }
                    }
                }
            }
        }
    }
}