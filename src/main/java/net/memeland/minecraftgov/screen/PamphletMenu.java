package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.item.custom.PamphletItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PamphletMenu extends AbstractContainerMenu {
    private final InteractionHand hand;
    private final ItemStack pamphletStack;

    // Server-side constructor
    public PamphletMenu(int id, Inventory inventory, InteractionHand hand, ItemStack pamphletStack) {
        super(ModMenuTypes.PAMPHLET_MENU.get(), id);
        this.hand = hand;
        this.pamphletStack = pamphletStack;
    }

    // Client-side constructor
    public PamphletMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.PAMPHLET_MENU.get(), id);
        this.hand = extraData.readEnum(InteractionHand.class);
        this.pamphletStack = extraData.readItem();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        ItemStack currentStack = player.getItemInHand(hand);
        return !currentStack.isEmpty() && currentStack.getItem() instanceof PamphletItem;
    }

    // Simple getters
    public InteractionHand getHand() {
        return hand;
    }

    public String getTitle() {
        return PamphletItem.getTitle(pamphletStack);
    }

    public String getContent() {
        return PamphletItem.getContent(pamphletStack);
    }

    public boolean canEdit() {
        return PamphletItem.canEdit(pamphletStack);
    }

    public boolean isSigned() {
        return PamphletItem.isSigned(pamphletStack);
    }

    public String getAuthor() {
        return PamphletItem.getAuthor(pamphletStack);
    }

    // Color methods for GUI tinting
    public boolean hasColor() {
        return PamphletItem.hasColor(pamphletStack);
    }

    public int getBackgroundColor() {
        return PamphletItem.getLightColor(pamphletStack);
    }

    public int getTextColor() {
        return PamphletItem.getTextColor(pamphletStack);
    }

    public String getColorName() {
        return PamphletItem.getColorName(pamphletStack);
    }

    public List<Integer> getSymbols() {
        return PamphletItem.getSymbols(pamphletStack);
    }
}