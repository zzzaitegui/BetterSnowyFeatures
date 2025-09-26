package net.memeland.minecraftgov.block.entity;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModgovMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BallotBoxBlockEntity>> BALLOT_BOX =
            BLOCK_ENTITIES.register("ballot_box", () ->
                    BlockEntityType.Builder.of(BallotBoxBlockEntity::new,
                            ModBlocks.BALLOT_BOX_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BulletinBoardBlockEntity>> BULLETIN_BOARD =
            BLOCK_ENTITIES.register("bulletin_board", () ->
                    BlockEntityType.Builder.of(BulletinBoardBlockEntity::new,
                            ModBlocks.BULLETIN_BOARD_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
