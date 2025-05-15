package net.memeland.templatemod.block;

import net.memeland.templatemod.TemplateMod;
import net.memeland.templatemod.item.ModCreativeModeTab;
import net.memeland.templatemod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TemplateMod.MOD_ID);

    // Return a registry object for further use
    // A registry object is a container that holds a reference to a registered object like a block, item. etc.
    public static final RegistryObject<Block> ALMANDINE_BLOCK = registerBlock("almandine_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST)
                    .strength(4.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)),
            ModCreativeModeTab.TEMPLATE_TAB);

    // This helper method registers a block with Minecraft, takes a name supplier (creates a block with properties) and tab
    // It also calls the registerBlockItem to register its related item form
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    //This helper method register an item form of a block, uses ModItems registry to create a BlockItem that represents the block in inventory
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
