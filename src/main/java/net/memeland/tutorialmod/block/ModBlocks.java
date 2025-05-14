package net.memeland.tutorialmod.block;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.custom.*;
import net.memeland.tutorialmod.fluid.ModFluids;
import net.memeland.tutorialmod.item.ModCreativeModeTab;
import net.memeland.tutorialmod.item.ModItems;
import net.memeland.tutorialmod.world.feature.tree.LarchTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TutorialMod.MOD_ID);

    // Return a registry object for further use
    // A registry object is a container that holds a reference to a registered object like a block, item. etc.
    public static final RegistryObject<Block> ALMANDINE_BLOCK = registerBlock("almandine_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST)
                    .strength(4.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> ALMANDINE_ORE = registerBlock("almandine_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 7)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> DEEPSLATE_ALMANDINE_ORE = registerBlock("deepslate_almandine_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4.5f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 7)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> NETHER_ALMANDINE_ORE = registerBlock("nether_almandine_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(1.0f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 7)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> ENDSTONE_ALMANDINE_ORE = registerBlock("endstone_almandine_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4.0f)
                    .requiresCorrectToolForDrops(), UniformInt.of(4, 9)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> ALMANDINE_LAMP = registerBlock("almandine_lamp",
            () -> new AlmandineLampBlock(BlockBehaviour.Properties.of(Material.GLASS)
                    .strength(1.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.GLASS)
                    .lightLevel(state -> state.getValue(AlmandineLampBlock.LIT) ? 15 : 0)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4.5f)
                    .requiresCorrectToolForDrops()),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> JUREMA_CROP = BLOCKS.register("jurema_crop",
            () -> new JuremaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static final RegistryObject<LiquidBlock> TROPICAL_WATER_BLOCK = BLOCKS.register("tropical_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_TROPICAL_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)));
                                                                    // No Collision, Strength and No loot table should be added by copying WATER properties

    /* LARCH TREE BLOCKS */

    public static final RegistryObject<Block> DRYING_TRAY = registerBlock("drying_tray",
            () -> new DryingTrayBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(4.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> LARCH_LOG = registerBlock("larch_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LOG)
                    .strength(6.5f) // Custom strength, we could get it from SPRUCE_LOG as default
                    .requiresCorrectToolForDrops()), // We could get it from SPRUCE_LOG as default
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> LARCH_WOOD = registerBlock("larch_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_WOOD)
                    .requiresCorrectToolForDrops()),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> STRIPPED_LARCH_LOG = registerBlock("stripped_larch_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_LOG)
                    .requiresCorrectToolForDrops()),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> STRIPPED_LARCH_WOOD = registerBlock("stripped_larch_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD)
                    .requiresCorrectToolForDrops()),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> LARCH_PLANKS = registerBlock("larch_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .requiresCorrectToolForDrops()){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }
            },
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> LARCH_LEAVES = registerBlock("larch_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES)
                    .requiresCorrectToolForDrops()){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }
            },
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> LARCH_SAPLING = registerBlock("larch_sapling",
            () -> new SaplingBlock(new LarchTreeGrower(), BlockBehaviour.Properties.copy(Blocks.SPRUCE_SAPLING)),
            ModCreativeModeTab.TUTORIAL_TAB);

    /* END OF LARCH TREE BLOCKS */

    public static final RegistryObject<Block> MORNING_GLORY = registerBlock("morning_glory",
            () -> new FlowerBlock(MobEffects.GLOWING, 5, BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)),
            ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> POTTED_MORNING_GLORY = BLOCKS.register("potted_morning_glory",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.MORNING_GLORY, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));

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
