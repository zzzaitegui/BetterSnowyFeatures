package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedFernBlock extends TallGrassBlock {
    private final String essenceName;

    public TintedFernBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.FERN)
                .noCollission()
                .instabreak()
                .offsetType(OffsetType.XYZ));
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