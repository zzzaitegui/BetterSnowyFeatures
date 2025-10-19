package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedSugarCaneBlock extends SugarCaneBlock {
    private final String essenceName;

    public TintedSugarCaneBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.SUGAR_CANE));
        this.essenceName = essenceName;
    }

    public String getEssenceName() {
        return essenceName;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true; // Allow placement on any block
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Override to prevent growth - do nothing
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }
}