package net.memeland.tutorialmod.networking.packet;

import net.memeland.tutorialmod.block.entity.DryingTrayBlockEntity;
import net.memeland.tutorialmod.screen.DryingTrayMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Client to Server C 2 S
public class FluidSyncS2CPacket {
    private final FluidStack fluidStack;
    private final BlockPos blockPos;

    public FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.blockPos = pos;
    }

    public FluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT
            if (Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof DryingTrayBlockEntity blockEntity) {
                blockEntity.setFluid(this.fluidStack);

                if(Minecraft.getInstance().player.containerMenu instanceof DryingTrayMenu menu && menu.getBlockEntity().getBlockPos().equals(blockPos)) {
                    menu.setFluid(this.fluidStack);
                }
            }
        });
        return true;
    }
}
