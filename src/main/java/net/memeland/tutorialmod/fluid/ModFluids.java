package net.memeland.tutorialmod.fluid;

import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.block.ModBlocks;
import net.memeland.tutorialmod.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, TutorialMod.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_TROPICAL_WATER = FLUIDS.register("tropical_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.TROPICAL_WATER_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_TROPICAL_WATER = FLUIDS.register("flowing_tropical_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TROPICAL_WATER_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties TROPICAL_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.TROPICAL_WATER_FLUID_TYPE, SOURCE_TROPICAL_WATER, FLOWING_TROPICAL_WATER)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .block(ModBlocks.TROPICAL_WATER_BLOCK)
            .bucket(ModItems.TROPICAL_WATER_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
