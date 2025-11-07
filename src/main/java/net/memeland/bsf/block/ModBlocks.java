package net.memeland.bsf.block;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.block.custom.IcicleBlock;
import net.memeland.bsf.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, BetterSnowyFeaturesMod.MOD_ID);

    public static final DeferredHolder<Block, Block> ICICLE = registerBlock("icicle",
            () -> new IcicleBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .noCollission()));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Item, Item> registerBlockItem(String name, Supplier<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}