package net.memeland.tutorialmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EightBallItem extends Item {

    public EightBallItem(Properties properties) {
        super(properties);
    }

    // What do we want to happen when we right-click with this item in hand?
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            //Output random number
            outputRandomNumber(player);
            //Set a cooldown
            player.getCooldowns().addCooldown(this, 20);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltip.tutorialmod.eightball_info").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.translatable("tooltip.tutorialmod.eightball_hint").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, components, flag);
    }

    private void outputRandomNumber(Player player) {
        player.sendSystemMessage(Component.literal("Your Number is "+ getRandowNumber()));
    }

    // Method for the random 0-10 number
    private int getRandowNumber() {
        return RandomSource.createNewThreadLocalInstance().nextInt(10);
    }
}
