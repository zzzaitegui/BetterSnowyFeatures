package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ArcticPlantBlock extends BushBlock {

    public ArcticPlantBlock() {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.GRASS)
                .noCollission()
                .instabreak()
                .offsetType(OffsetType.XYZ));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}