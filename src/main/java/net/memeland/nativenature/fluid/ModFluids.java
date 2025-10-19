package net.memeland.nativenature.fluid;

import net.memeland.nativenature.NativeNatureMod;
import net.memeland.nativenature.fluid.custom.*;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, NativeNatureMod.MOD_ID);

    // GLACIAL WATER
    public static final RegistryObject<FlowingFluid> GLACIAL_WATER_STILL = FLUIDS.register("glacial_water_still",
            GlacialWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> GLACIAL_WATER_FLOWING = FLUIDS.register("glacial_water_flowing",
            GlacialWaterFluid.Flowing::new);

    // RIVER WATER
    public static final RegistryObject<FlowingFluid> RIVER_WATER_STILL = FLUIDS.register("river_water_still",
            RiverWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> RIVER_WATER_FLOWING = FLUIDS.register("river_water_flowing",
            RiverWaterFluid.Flowing::new);

    // WETLANDS WATER
    public static final RegistryObject<FlowingFluid> WETLANDS_WATER_STILL = FLUIDS.register("wetlands_water_still",
            WetlandsWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> WETLANDS_WATER_FLOWING = FLUIDS.register("wetlands_water_flowing",
            WetlandsWaterFluid.Flowing::new);

    // RAINFOREST WATER
    public static final RegistryObject<FlowingFluid> RAINFOREST_WATER_STILL = FLUIDS.register("rainforest_water_still",
            RainforestWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> RAINFOREST_WATER_FLOWING = FLUIDS.register("rainforest_water_flowing",
            RainforestWaterFluid.Flowing::new);

    // TROPICAL WATER
    public static final RegistryObject<FlowingFluid> TROPICAL_WATER_STILL = FLUIDS.register("tropical_water_still",
            TropicalWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> TROPICAL_WATER_FLOWING = FLUIDS.register("tropical_water_flowing",
            TropicalWaterFluid.Flowing::new);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}