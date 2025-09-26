package net.memeland.minecraftgov.block.entity;

import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.data.PartyManager;
import net.memeland.minecraftgov.screen.BulletinBoardMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BulletinBoardBlockEntity extends BlockEntity implements MenuProvider {
    public static final Component TITLE = Component.translatable("container.modgov.bulletin_board");

    public BulletinBoardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BULLETIN_BOARD.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BulletinBoardMenu(id, inventory, this, this.worldPosition);
    }

    public boolean registerParty(String name, int symbolId, DyeColor color, String representative) {
        if (level instanceof ServerLevel serverLevel) {
            PartyManager manager = PartyManager.get(serverLevel);
            PartyData party = new PartyData(name.trim(), symbolId, color, representative);
            return manager.registerParty(party);
        }
        return false;
    }

    public Collection<PartyData> getAllParties() {
        if (level instanceof ServerLevel serverLevel) {
            PartyManager manager = PartyManager.get(serverLevel);
            return manager.getAllParties();
        }
        return java.util.Collections.emptyList();
    }

    public boolean canRegisterMoreParties() {
        if (level instanceof ServerLevel serverLevel) {
            PartyManager manager = PartyManager.get(serverLevel);
            return manager.canRegisterMoreParties();
        }
        return false;
    }

    public int getPartyCount() {
        if (level instanceof ServerLevel serverLevel) {
            PartyManager manager = PartyManager.get(serverLevel);
            return manager.getPartyCount();
        }
        return 0;
    }
}