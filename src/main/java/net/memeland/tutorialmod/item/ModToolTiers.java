package net.memeland.tutorialmod.item;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static Tier ALMANDINE;

    static {
        ALMANDINE = TierSortingRegistry.registerTier(
                new ForgeTier(5, 2200, 10.0f, 4.0f, 24, ModTags.Blocks.NEEDS_ALMANDINE_TOOL,
                        () -> Ingredient.of(ModItems.ALMANDINE.get())),
                ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "almandine"), List.of(Tiers.NETHERITE), List.of());
    }
}
