package net.memeland.nativenature.fluid;

import net.memeland.nativenature.NativeNatureMod;
import org.joml.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.withDefaultNamespace("misc/in_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, NativeNatureMod.MOD_ID);

    private static FluidType.Properties createWaterProperties() {
        return FluidType.Properties.create()
                .density(50)
                .viscosity(50)
                .canSwim(true)
                .canDrown(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .canExtinguish(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }

    // Glacial Water
    public static final RegistryObject<FluidType> GLACIAL_WATER_TYPE = FLUID_TYPES.register("glacial_water",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF3938C9, new Vector3f(57f / 255f, 56f / 255f, 201f / 255f), createWaterProperties()));

    // River Water
    public static final RegistryObject<FluidType> RIVER_WATER_TYPE = FLUID_TYPES.register("river_water",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF3F76E4, new Vector3f(63f / 255f, 118f / 255f, 228f / 255f), createWaterProperties()));

    // Wetlands Water (Mangrove Swamp)
    public static final RegistryObject<FluidType> WETLANDS_WATER_TYPE = FLUID_TYPES.register("wetlands_water",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF3A7A6A, new Vector3f(58f / 255f, 122f / 255f, 106f / 255f), createWaterProperties()));

    // Rainforest Water (Turquoise)
    public static final RegistryObject<FluidType> RAINFOREST_WATER_TYPE = FLUID_TYPES.register("rainforest_water",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF43D5EE, new Vector3f(67f / 255f, 213f / 255f, 238f / 255f), createWaterProperties()));

    // Tropical Water (Warm Ocean)
    public static final RegistryObject<FluidType> TROPICAL_WATER_TYPE = FLUID_TYPES.register("tropical_water",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFC39B1DD, new Vector3f(32f / 255f, 92f / 255f, 119f / 255f), createWaterProperties()));

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}