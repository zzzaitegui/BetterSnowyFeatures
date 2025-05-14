package net.memeland.tutorialmod.networking.packet;

import net.memeland.tutorialmod.block.entity.DryingTrayBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

// Client to Server C 2 S
public class ItemStackSyncS2CPacket {
    private final ItemStackHandler itemStackHandler;
    private final BlockPos blockPos;

    public ItemStackSyncS2CPacket(ItemStackHandler itemStackHandler, BlockPos pos) {
        this.itemStackHandler = itemStackHandler;
        this.blockPos = pos;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        itemStackHandler = new ItemStackHandler(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            itemStackHandler.insertItem(1, collection.get(i), false);
        }

        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        Collection<ItemStack> list= new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.add(itemStackHandler.getStackInSlot(i));
        }

        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT
            if (Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof DryingTrayBlockEntity blockEntity) {
                blockEntity.setHandler(this.itemStackHandler);


            }
        });
        return true;
    }
}
