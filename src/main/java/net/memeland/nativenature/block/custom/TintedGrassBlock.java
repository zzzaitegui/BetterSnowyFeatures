package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TintedGrassBlock extends GrassBlock {
    private final String essenceName;

    public TintedGrassBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.GRASS_BLOCK));
        this.essenceName = essenceName;
    }

    public String getEssenceName() {
        return essenceName;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Override to prevent spreading - do nothing
    }
}