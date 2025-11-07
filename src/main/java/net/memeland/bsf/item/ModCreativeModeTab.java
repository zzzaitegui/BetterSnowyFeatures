package net.memeland.bsf.item;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BetterSnowyFeaturesMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BSF_TAB = CREATIVE_MODE_TABS.register("bsf_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ICICLE.get()))
                    .title(Component.literal("Better Snowy Features"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.ICICLE.get());
                    })
                    .build()
    );
}