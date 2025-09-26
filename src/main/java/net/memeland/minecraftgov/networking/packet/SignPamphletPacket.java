package net.memeland.minecraftgov.networking.packet;

import net.memeland.minecraftgov.item.custom.PamphletItem;
import net.memeland.minecraftgov.networking.ModMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SignPamphletPacket {
    private final InteractionHand hand;

    public SignPamphletPacket(InteractionHand hand) {
        this.hand = hand;
    }

    public SignPamphletPacket(FriendlyByteBuf buf) {
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(hand);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player == null) return;

            // Rate limiting check
            if (player != null && ModMessages.isOnCooldown(player.getUUID(), this.getClass())) {
                return;
            }

            ItemStack pamphletStack = player.getItemInHand(hand);
            if (pamphletStack.isEmpty() || !(pamphletStack.getItem() instanceof PamphletItem)) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.no_pamphlet").withStyle(ChatFormatting.RED));
                return;
            }

            if (PamphletItem.isSigned(pamphletStack)) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.already_signed").withStyle(ChatFormatting.RED));
                return;
            }

            // Check if there's content to sign
            String title = PamphletItem.getTitle(pamphletStack);
            String content = PamphletItem.getContent(pamphletStack);
            if (title.trim().isEmpty() && content.trim().isEmpty()) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.empty").withStyle(ChatFormatting.RED));
                return;
            }

            // Sign the pamphlet
            PamphletItem.sign(pamphletStack, player);
            player.sendSystemMessage(Component.translatable("pamphlet.modgov.signed_success").withStyle(ChatFormatting.GREEN));
        });
        return true;
    }
}