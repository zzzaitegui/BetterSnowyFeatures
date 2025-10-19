package net.memeland.nativenature.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedPlantBlock extends BushBlock {
    private final String essenceName;

    public TintedPlantBlock(String essenceName) {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.GRASS)
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