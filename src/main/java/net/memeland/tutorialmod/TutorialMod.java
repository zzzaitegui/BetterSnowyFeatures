package net.memeland.tutorialmod;

import net.memeland.tutorialmod.block.ModBlocks;
import net.memeland.tutorialmod.block.entity.ModBlockEntities;
import net.memeland.tutorialmod.entity.ModEntityTypes;
import net.memeland.tutorialmod.entity.client.GenetRenderer;
import net.memeland.tutorialmod.fluid.ModFluidTypes;
import net.memeland.tutorialmod.fluid.ModFluids;
import net.memeland.tutorialmod.item.ModItems;
import net.memeland.tutorialmod.loot.ModLootModifiers;
import net.memeland.tutorialmod.networking.ModMessages;
import net.memeland.tutorialmod.painting.ModPaintings;
import net.memeland.tutorialmod.recipe.ModRecipes;
import net.memeland.tutorialmod.screen.DryingTrayScreen;
import net.memeland.tutorialmod.screen.ModMenuTypes;
import net.memeland.tutorialmod.villager.ModVillagers;
import net.memeland.tutorialmod.world.feature.ModConfiguredFeatures;
import net.memeland.tutorialmod.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TutorialMod.MOD_ID)

public class TutorialMod {

    public static final String MOD_ID = "tutorialmod";

    public TutorialMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModVillagers.register(modEventBus);

        ModPaintings.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);

        ModPlacedFeatures.register(modEventBus);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        GeckoLib.initialize();
        ModEntityTypes.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityTypes.GENET.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Monster::checkMonsterSpawnRules);

            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MORNING_GLORY.getId(), ModBlocks.POTTED_MORNING_GLORY);
            ModMessages.register();
            ModVillagers.registerPOIs();
        });

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TROPICAL_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TROPICAL_WATER.get(), RenderType.translucent());

            MenuScreens.register(ModMenuTypes.DRYING_TRAY_MENU.get(), DryingTrayScreen::new);

            EntityRenderers.register(ModEntityTypes.GENET.get(), GenetRenderer::new);
        }
    }
}
