package net.memeland.minecraftgov.item;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModgovMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MODGOV_TAB = CREATIVE_MODE_TABS.register("modgov_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ID_CARD.get()))
                    .title(Component.literal("Mod.gov"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ID_CARD.get());
                        output.accept(ModItems.PAMPHLET.get());
                        output.accept(ModItems.BALLOT.get());
                        output.accept(ModBlocks.BALLOT_BOX_BLOCK.get());
                        output.accept(ModBlocks.BULLETIN_BOARD_BLOCK.get());
                    })
                    .build()
    );
}