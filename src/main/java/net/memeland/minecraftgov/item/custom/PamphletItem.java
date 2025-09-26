package net.memeland.minecraftgov.item.custom;

import net.memeland.minecraftgov.screen.PamphletMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PamphletItem extends Item {

    public PamphletItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        // Signed pamphlets can stack to 64 for mass distribution
        // Unsigned pamphlets stay at 1
        return isSigned(stack) ? 64 : 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider menuProvider = new PamphletMenuProvider(stack, hand);
            NetworkHooks.openScreen(serverPlayer, menuProvider, buf -> {
                buf.writeEnum(hand);
                buf.writeItem(stack);
            });
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        if (isSigned(stack)) {
            String author = getAuthor(stack);
            if (!author.isEmpty()) {
                tooltipComponents.add(Component.translatable("book.byAuthor", author).withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltipComponents.add(Component.translatable("gui.modgov.pamphlet.unsigned").withStyle(ChatFormatting.GRAY));
        }
    }

    // NBT methods
    public static String getTitle(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getString("pamphlet_title") : "";
    }

    public static void setTitle(ItemStack stack, String title) {
        String limitedTitle = title.length() > 41 ? title.substring(0, 41) : title;
        stack.getOrCreateTag().putString("pamphlet_title", title);
        if (isSigned(stack) && !title.isEmpty()) {
            stack.setHoverName(Component.literal(title));
        }
    }

    public static List<Integer> getSymbols(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("pamphlet_symbols")) {
            int[] symbolArray = tag.getIntArray("pamphlet_symbols");
            List<Integer> symbols = new ArrayList<>();
            for (int symbol : symbolArray) {
                symbols.add(symbol);
            }
            return symbols;
        }
        return new ArrayList<>();
    }

    public static void setSymbols(ItemStack stack, List<Integer> symbols) {
        int[] symbolArray = symbols.stream().mapToInt(Integer::intValue).toArray();
        stack.getOrCreateTag().putIntArray("pamphlet_symbols", symbolArray);
    }

    public static String getContent(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getString("pamphlet_content") : "";
    }

    public static void setContent(ItemStack stack, String content) {
        String limitedContent = content.length() > 500 ? content.substring(0, 500) : content;
        stack.getOrCreateTag().putString("pamphlet_content", content);
    }

    public static boolean isSigned(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean("pamphlet_signed");
    }

    public static String getAuthor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getString("pamphlet_author") : "";
    }

    public static boolean canEdit(ItemStack stack) {
        return !isSigned(stack);
    }

    public static void sign(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("pamphlet_signed", true);
        tag.putString("pamphlet_author", player.getGameProfile().getName());

        String title = getTitle(stack);
        if (!title.isEmpty()) {
            stack.setHoverName(Component.literal(title));
        }
    }

    // Color methods (custom NBT instead of DyeableLeatherItem)
    public static String getColorName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getString("pamphlet_color") : "";
    }

    public static void setColor(ItemStack stack, DyeColor dyeColor) {
        stack.getOrCreateTag().putString("pamphlet_color", dyeColor.getName());

        // Set CustomModelData for texture switching
        int modelData = getModelDataForColor(dyeColor);
        if (modelData > 0) {
            stack.getOrCreateTag().putInt("CustomModelData", modelData);
        }
    }

    private static int getModelDataForColor(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> 1;
            case ORANGE -> 2;
            case MAGENTA -> 3;
            case LIGHT_BLUE -> 4;
            case YELLOW -> 5;
            case LIME -> 6;
            case PINK -> 7;
            case GRAY -> 8;
            case LIGHT_GRAY -> 9;
            case CYAN -> 10;
            case PURPLE -> 11;
            case BLUE -> 12;
            case BROWN -> 13;
            case GREEN -> 14;
            case RED -> 15;
            case BLACK -> 16;
        };
    }

    public static boolean hasColor(ItemStack stack) {
        return !getColorName(stack).isEmpty();
    }

    public static int getRGBColor(ItemStack stack) {
        String colorName = getColorName(stack);
        if (colorName.isEmpty()) {
            return 0xF9FFFE; // Default white
        }

        // Convert color name back to DyeColor and get RGB
        try {
            DyeColor dyeColor = DyeColor.byName(colorName, DyeColor.WHITE);
            return dyeColor.getTextColor();
        } catch (Exception e) {
            return 0xF9FFFE; // Fallback to white
        }
    }

    // Helper method for GUI background (light version)
    public static int getLightColor(ItemStack stack) {
        int originalColor = getRGBColor(stack);

        // Extract RGB components
        int r = (originalColor >> 16) & 0xFF;
        int g = (originalColor >> 8) & 0xFF;
        int b = originalColor & 0xFF;

        // Special handling for black - almost pitch black background
        if (r <= 10 && g <= 10 && b <= 10) {
            return 0xFF141414; // Very dark gray (RGB 20,20,20)
        }

        // Lighten (30% original, 70% white)
        r = (int)(r * 0.3f + 255 * 0.7f);
        g = (int)(g * 0.3f + 255 * 0.7f);
        b = (int)(b * 0.3f + 255 * 0.7f);

        return 0xFF000000 | (Math.min(255, r) << 16) | (Math.min(255, g) << 8) | Math.min(255, b);
    }

    // Helper method for text color (dark version)
    public static int getTextColor(ItemStack stack) {
        int originalColor = getRGBColor(stack);

        // Extract RGB components
        int r = (originalColor >> 16) & 0xFF;
        int g = (originalColor >> 8) & 0xFF;
        int b = originalColor & 0xFF;

        // Special handling for black - very light gray text
        if (r <= 10 && g <= 10 && b <= 10) {
            return 0xFFE8E8E8; // Very light gray (RGB 232,232,232)
        }

        // Darken (70% original)
        r = (int)(r * 0.7f);
        g = (int)(g * 0.7f);
        b = (int)(b * 0.7f);

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private static class PamphletMenuProvider implements MenuProvider {
        private final ItemStack stack;
        private final InteractionHand hand;

        public PamphletMenuProvider(ItemStack stack, InteractionHand hand) {
            this.stack = stack;
            this.hand = hand;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.modgov.pamphlet");
        }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
            return new PamphletMenu(id, inventory, hand, stack);
        }
    }
}