package net.memeland.minecraftgov.block.entity;

import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.data.VoteManager;
import net.memeland.minecraftgov.screen.BallotBoxMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class BallotBoxBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private final LazyOptional<IItemHandlerModifiable> optional = LazyOptional.of(() -> this.inventory);

    private final ContainerData DATA = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return 1;
        }

        @Override
        public void set(int pIndex, int pValue) {

        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public static final Component TITLE = Component.translatable("container.modgov.ballot_box");

    public BallotBoxBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BALLOT_BOX.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BallotBoxMenu(id, inventory, this, this.worldPosition);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.optional.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public ContainerData getContainerData() {
        return this.DATA;
    }

    // Vote-related methods

    /**
     * Get all available parties for voting
     */
    public Collection<PartyData> getAvailableParties() {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            voteManager.initializePartiesIfNeeded(serverLevel);
            return voteManager.getAvailableParties();
        } else {
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Get all party votes and their counts
     */
    public Map<String, Integer> getAllVotes() {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            voteManager.initializePartiesIfNeeded(serverLevel);
            return voteManager.getAllVotes();
        }
        return java.util.Collections.emptyMap();
    }

    /**
     * Get vote percentage for a specific party
     */
    public double getVotePercentage(String partyName) {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            voteManager.initializePartiesIfNeeded(serverLevel);
            return voteManager.getVotePercentage(partyName);
        }
        return 0.0;
    }

    /**
     * Get total number of votes cast
     */
    public int getTotalVotes() {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            return voteManager.getTotalVotes();
        }
        return 0;
    }

    /**
     * Check if a player has already voted
     */
    public boolean hasPlayerVoted(Player player) {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            return voteManager.hasVoted(player.getUUID());
        }
        return false;
    }

    /**
     * Check if voting has started (parties are frozen)
     */
    public boolean hasVotingStarted() {
        if (level instanceof ServerLevel serverLevel) {
            VoteManager voteManager = VoteManager.get(serverLevel);
            return voteManager.hasVotingStarted();
        }
        return false;
    }
}