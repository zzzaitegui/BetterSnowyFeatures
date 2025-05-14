package net.memeland.tutorialmod.item;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.ModBlocks;
import net.memeland.tutorialmod.block.custom.JuremaCropBlock;
import net.memeland.tutorialmod.entity.ModEntityTypes;
import net.memeland.tutorialmod.fluid.ModFluids;
import net.memeland.tutorialmod.item.custom.EightBallItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MOD_ID);

    public static final RegistryObject<Item> ALMANDINE = ITEMS.register("almandine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)));

    public static final RegistryObject<Item> IMPURE_ALMANDINE = ITEMS.register("impure_almandine",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)));

    public static final RegistryObject<Item> EIGHT_BALL = ITEMS.register("eight_ball",
            () -> new EightBallItem(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).stacksTo(1)));

    public static final RegistryObject<Item> JUREMA_SEEDS = ITEMS.register("jurema_seeds",
            () -> new ItemNameBlockItem(ModBlocks.JUREMA_CROP.get(), new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)) {
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
                    components.add(Component.translatable("tooltip.tutorialmod.jurema_seeds_hint").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(stack, level, components, flag);
                }
            });

    public static final RegistryObject<Item> JUREMA_LEAVES = ITEMS.register("jurema_leaves",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)
                            .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(2f).build())) {
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
                    components.add(Component.translatable("tooltip.tutorialmod.jurema_leaves_hint").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(stack, level, components, flag);
                }
            });

    public static final RegistryObject<Item> JUREMA_BARK = ITEMS.register("jurema_bark",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)) {
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
                    components.add(Component.translatable("tooltip.tutorialmod.jurema_bark_hint").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(stack, level, components, flag);
                }
            });

    public static final RegistryObject<Item> DRIED_JUREMA_BARK = ITEMS.register("dried_jurema_bark",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB)) {
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
                    components.add(Component.translatable("tooltip.tutorialmod.dried_jurema_bark_hint").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(stack, level, components, flag);
                }
            });

    public static final RegistryObject<BucketItem> TROPICAL_WATER_BUCKET = ITEMS.register("tropical_water_bucket", //craftRemainder should already be done bt the new BucketItem but just to be sure
            () -> new BucketItem(ModFluids.SOURCE_TROPICAL_WATER, new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<SwordItem> GREAT_KATANA = ITEMS.register("great_katana",
            () -> new SwordItem(Tiers.NETHERITE, 8, -1.5f, new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).stacksTo(1)));

    public static final RegistryObject<PickaxeItem> ALMANDINE_PICKAXE = ITEMS.register("almandine_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ALMANDINE, 2, -2.6f, new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).stacksTo(1))); // StacksTo should be done automatically

    public static final RegistryObject<Item> GENET_SPAWN_EGG = ITEMS.register("genet_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GENET, 0xFFA29686, 0xFF180E0A, new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
