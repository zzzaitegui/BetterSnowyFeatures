package net.memeland.minecraftgov.block.custom;

import net.memeland.minecraftgov.block.ModBlocks;
import net.memeland.minecraftgov.block.entity.BallotBoxBlockEntity;
import net.memeland.minecraftgov.block.entity.ModBlockEntities;
import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.data.VoteManager;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.VoteDataSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BallotBoxBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(0.7, 0, 0.7, 15.3, 12.86, 15.3);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public BallotBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof BallotBoxBlockEntity ballotBox) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                ServerLevel serverLevel = (ServerLevel) level;

                double distance = player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                if (distance > 64) { // 8 block range
                    return InteractionResult.FAIL;
                }

                VoteManager voteManager = VoteManager.get(serverLevel);
                voteManager.initializePartiesIfNeeded(serverLevel);

                List<PartyData> availableParties = new ArrayList<>(voteManager.getAvailableParties());
                Map<String, Integer> voteData = voteManager.getAllVotes();
                boolean hasPlayerVoted = voteManager.hasVoted(player.getUUID());
                int totalVotes = voteManager.getTotalVotes();

                VoteDataSyncPacket syncPacket = new VoteDataSyncPacket(
                        blockPos, availableParties, voteData, hasPlayerVoted, totalVotes
                );
                ModMessages.sendToPlayer(syncPacket, serverPlayer);

                NetworkHooks.openScreen(serverPlayer, ballotBox, buf -> {
                    buf.writeBlockPos(blockPos);
                });

            } else {
                throw new IllegalStateException("Our container provider for BallotBoxBlockEntity is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.BALLOT_BOX.get().create(pPos, pState);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            if (!player.getAbilities().instabuild) {
                ItemEntity blockDrop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(ModBlocks.BALLOT_BOX_BLOCK.get()));
                level.addFreshEntity(blockDrop);
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BallotBoxBlockEntity ballotBox) {
                for (int i = 0; i < ballotBox.getInventory().getSlots(); i++) {
                    ItemStack stack = ballotBox.getInventory().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        ItemEntity itemDrop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
                        level.addFreshEntity(itemDrop);
                    }
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return new ArrayList<>();
    }
}