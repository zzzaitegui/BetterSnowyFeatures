package net.memeland.tutorialmod.fluid;

import com.mojang.math.Vector3f;
import net.memeland.tutorialmod.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
    public static final ResourceLocation TROPICAL_OVERLAY_RL = ResourceLocation.withDefaultNamespace("misc/in_tropical_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<FluidType> TROPICAL_WATER_FLUID_TYPE = register("tropical_water_fluid", FluidType.Properties.create()
            .lightLevel(2)
            .density(15)
            .viscosity(5)
            .sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, TROPICAL_OVERLAY_RL,
                0xFF3FCFCB, new Vector3f(79f / 255f, 208f / 255f, 213f / 255f), properties)); //FF alpha value, 3F red, CF green, CB blue
                                                //Vector3f RGB values go from 0 to 1 instead of 0 to 255 so we need to convert 79 to 0,309 fpr example
    }


    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
