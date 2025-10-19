package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedVineBlock extends VineBlock {
    private final String essenceName;

    public TintedVineBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.VINE));
        this.essenceName = essenceName;
    }

    public String getEssenceName() {
        return essenceName;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Override to prevent spreading - do nothing
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }
}