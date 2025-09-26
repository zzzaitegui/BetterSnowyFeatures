package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.block.ModBlocks;
import net.memeland.minecraftgov.block.entity.BulletinBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class BulletinBoardMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess levelAccess;
    private final BulletinBoardBlockEntity blockEntity;
    private final BlockPos blockPos;

    // Server-side constructor
    public BulletinBoardMenu(int pContainerId, Inventory playerInv, BulletinBoardBlockEntity blockEntity, BlockPos pos) {
        super(ModMenuTypes.BULLETIN_BOARD_MENU.get(), pContainerId);
        this.levelAccess = ContainerLevelAccess.create(playerInv.player.getLevel(), pos);
        this.blockEntity = blockEntity;
        this.blockPos = pos;
    }

    // Client-side constructor (receives BlockPos from server via FriendlyByteBuf)
    public BulletinBoardMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ModMenuTypes.BULLETIN_BOARD_MENU.get(), id);
        this.blockEntity = null;
        this.blockPos = extraData.readBlockPos();
        this.levelAccess = ContainerLevelAccess.create(inv.player.getLevel(), this.blockPos);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, ModBlocks.BULLETIN_BOARD_BLOCK.get());
    }

    public BulletinBoardBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}