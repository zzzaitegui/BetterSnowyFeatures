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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UpdatePamphletPacket {
    private final InteractionHand hand;
    private final String title;
    private final String content;
    private final List<Integer> symbols;

    public UpdatePamphletPacket(InteractionHand hand, String title, String content, List<Integer> symbols) {
        this.hand = hand;
        this.title = title;
        this.content = content;
        this.symbols = symbols;
    }

    public UpdatePamphletPacket(FriendlyByteBuf buf) {
        this.hand = buf.readEnum(InteractionHand.class);
        this.title = buf.readUtf(100); // Reasonable limit
        this.content = buf.readUtf(1000); // Reasonable limit

        // Read symbols
        int symbolCount = buf.readInt();
        this.symbols = new ArrayList<>();
        for (int i = 0; i < symbolCount; i++) {
            this.symbols.add(buf.readInt());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(hand);
        buf.writeUtf(title, 100);
        buf.writeUtf(content, 1000);

        // Write symbols
        buf.writeInt(symbols.size());
        for (int symbol : symbols) {
            buf.writeInt(symbol);
        }
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

            if (!PamphletItem.canEdit(pamphletStack)) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.signed").withStyle(ChatFormatting.RED));
                return;
            }

            String existingAuthor = PamphletItem.getAuthor(pamphletStack);
            if (!existingAuthor.isEmpty() && !existingAuthor.equals(player.getGameProfile().getName())) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.not_owner").withStyle(ChatFormatting.RED));
                return;
            }

            // Simple validation
            if (title.length() > 50 || content.length() > 500) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.too_long").withStyle(ChatFormatting.RED));
                return;
            }

            // Validate symbols
            if (symbols.size() > 4) {
                player.sendSystemMessage(Component.translatable("pamphlet.modgov.too_many_symbols").withStyle(ChatFormatting.RED));
                return;
            }

            for (int symbol : symbols) {
                if (symbol < 1 || symbol > 25) {
                    player.sendSystemMessage(Component.translatable("pamphlet.modgov.invalid_symbol").withStyle(ChatFormatting.RED));
                    return;
                }
            }

            // Update the pamphlet
            PamphletItem.setTitle(pamphletStack, title.trim());
            PamphletItem.setContent(pamphletStack, content.trim());
            PamphletItem.setSymbols(pamphletStack, symbols);

            player.sendSystemMessage(Component.translatable("pamphlet.modgov.update.success").withStyle(ChatFormatting.GREEN));
        });
        return true;
    }
}