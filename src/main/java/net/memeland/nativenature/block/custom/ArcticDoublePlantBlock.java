package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ArcticDoublePlantBlock extends DoublePlantBlock {

    public ArcticDoublePlantBlock() {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.TALL_GRASS)
                .noCollission()
                .instabreak()
                .offsetType(OffsetType.XZ));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}