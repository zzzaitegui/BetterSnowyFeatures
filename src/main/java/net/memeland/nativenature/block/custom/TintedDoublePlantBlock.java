package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedDoublePlantBlock extends DoublePlantBlock {
    private final String essenceName;

    public TintedDoublePlantBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.TALL_GRASS)
                .noCollission()
                .instabreak()
                .offsetType(OffsetType.XZ));
        this.essenceName = essenceName;
    }

    public String getEssenceName() {
        return essenceName;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return true; // Allow placement on any block
    }
}