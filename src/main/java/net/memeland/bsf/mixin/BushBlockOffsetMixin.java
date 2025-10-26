package net.memeland.bsf.mixin;

import net.memeland.bsf.ModConfig;
import net.memeland.bsf.util.BiomeTemperatureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Disables vanilla plant offset for grass/fern blocks when they're using snowy variants.
 * This works with both vanilla rendering AND Embeddium/Sodium optimized rendering.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BushBlockOffsetMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract BlockState asState();

    /**
     * Intercepts getOffset() to return zero offset when plant should use snowy model.
     * This prevents BOTH vanilla and Embeddium from applying random plant offset.
     */
    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
    public void bsf$disableOffsetForSnowyPlants(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        // Only affect plants (BushBlock subclasses)
        if (!(this.getBlock() instanceof BushBlock)) {
            return;
        }

        // Check if snowy plants feature is enabled
        if (!ModConfig.ENABLE_SNOWY_PLANTS.get()) {
            return;
        }

        try {
            // Check if we're in a cold biome
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && BiomeTemperatureHelper.isColdAt(mc.level, pos)) {
                // Check if plant has enough snow neighbors (same logic as SnowyPlantBakedModel)
                if (hasEnoughSnowNeighbors(level, pos, this.asState())) {
                    // Plant should use snowy variant - disable offset
                    cir.setReturnValue(Vec3.ZERO);
                }
            }
        } catch (Exception e) {
            // Fail silently - let vanilla offset apply
        }
    }

    private static boolean hasEnoughSnowNeighbors(BlockGetter level, BlockPos pos, BlockState state) {
        // For tall plants, check at ground level where snow is
        BlockPos checkPos = pos;
        if (state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF)) {
            if (state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF)
                    == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER) {
                checkPos = pos.below();
            }
        }

        int snowCount = 0;
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

        for (Direction direction : directions) {
            BlockPos adjacentPos = checkPos.relative(direction);

            try {
                BlockState adjacentState = level.getBlockState(adjacentPos);
                if (adjacentState.is(BlockTags.SNOW)) {
                    snowCount++;
                    if (snowCount >= 2) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // Skip this direction on error
            }
        }

        return false;
    }
}