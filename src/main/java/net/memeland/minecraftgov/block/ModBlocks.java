package net.memeland.minecraftgov.block;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.block.custom.BallotBoxBlock;
import net.memeland.minecraftgov.block.custom.BulletinBoardBlock;
import net.memeland.minecraftgov.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ModgovMod.MOD_ID);

    public static final RegistryObject<BallotBoxBlock> BALLOT_BOX_BLOCK = registerBlock("ballot_box",
            () -> new BallotBoxBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .destroyTime(2.0f)
                    .randomTicks()
                    .noOcclusion()
                    .dynamicShape()
                    .isValidSpawn((state, reader, pos, type) -> false)
                    .isRedstoneConductor((state, reader, pos) -> false)
                    .isSuffocating((state, reader, pos) -> false)
                    .isViewBlocking((state, reader, pos) -> false)));

    public static final RegistryObject<BulletinBoardBlock> BULLETIN_BOARD_BLOCK = registerBlock("bulletin_board",
            () -> new BulletinBoardBlock(BlockBehaviour.Properties.copy(Blocks.LOOM)
                    .randomTicks()
                    .noOcclusion()
                    .dynamicShape()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}