package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.block.ModBlocks;
import net.memeland.minecraftgov.block.entity.BallotBoxBlockEntity;
import net.memeland.minecraftgov.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BallotBoxMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess levelAccess;
    private final BallotBoxBlockEntity blockEntity;
    private final BlockPos blockPos;

    // Server-side constructor
    public BallotBoxMenu(int pContainerId, Inventory playerInv, BallotBoxBlockEntity blockEntity, BlockPos pos) {
        super(ModMenuTypes.BALLOT_BOX_MENU.get(), pContainerId);
        this.levelAccess = ContainerLevelAccess.create(playerInv.player.level(), pos);
        this.blockEntity = blockEntity;
        this.blockPos = pos;

        addBallotBoxSlots(blockEntity.getInventory());
        addPlayerHotbar(playerInv);
    }

    // Client-side constructor
    public BallotBoxMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ModMenuTypes.BALLOT_BOX_MENU.get(), id);
        this.blockEntity = null;
        this.blockPos = extraData.readBlockPos();
        this.levelAccess = ContainerLevelAccess.create(inv.player.level(), this.blockPos);

        addBallotBoxSlots(new ItemStackHandler(2));
        addPlayerHotbar(inv);
    }

    private void addBallotBoxSlots(IItemHandler itemHandler) {
        this.addSlot(new SlotItemHandler(itemHandler, 0, 90, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.BALLOT.get()) && !this.hasItem();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }

            @Override
            public ItemStack safeInsert(ItemStack stack) {
                if (!mayPlace(stack)) {
                    return stack;
                }
                ItemStack toInsert = stack.copy();
                toInsert.setCount(1);

                ItemStack remainder = super.safeInsert(toInsert);

                if (remainder.isEmpty()) {
                    ItemStack result = stack.copy();
                    result.shrink(1);
                    return result;
                } else {
                    return stack;
                }
            }
        });

        // ID Card slot
        this.addSlot(new SlotItemHandler(itemHandler, 1, 140, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.ID_CARD.get());
            }
        });
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 48 + i * 18, 232));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (pIndex < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack1.is(ModItems.BALLOT.get())) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.is(ModItems.ID_CARD.get())) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, ModBlocks.BALLOT_BOX_BLOCK.get());
    }

    public BallotBoxBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}