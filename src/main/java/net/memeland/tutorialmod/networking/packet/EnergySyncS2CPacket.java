package net.memeland.tutorialmod.networking.packet;

import net.memeland.tutorialmod.block.entity.DryingTrayBlockEntity;
import net.memeland.tutorialmod.client.ClientThirstData;
import net.memeland.tutorialmod.screen.DryingTrayMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Client to Server C 2 S
public class EnergySyncS2CPacket {
    private final int energy;
    private final BlockPos blockPos;

    public EnergySyncS2CPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.blockPos = pos;
    }

    public EnergySyncS2CPacket(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT
            if (Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof DryingTrayBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof DryingTrayMenu menu && menu.getBlockEntity().getBlockPos().equals(blockPos)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }
        });
        return true;
    }
}
