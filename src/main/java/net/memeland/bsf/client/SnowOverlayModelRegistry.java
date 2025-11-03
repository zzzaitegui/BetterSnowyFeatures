package net.memeland.bsf.client;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.client.model.SnowOverlayBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = BetterSnowyFeaturesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SnowOverlayModelRegistry {

    private static final Block[] OVERLAY_BLOCKS = {
            Blocks.STONE,
            Blocks.GRAVEL,
            Blocks.SPRUCE_PLANKS,
            Blocks.BLUE_ICE,
            Blocks.SAND,
            Blocks.PUMPKIN
    };

    private static final Map<String, ResourceLocation> SNOWY_TEXTURE_MODELS = new HashMap<>();

    static {
        SNOWY_TEXTURE_MODELS.put("stone", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_stone"));
        SNOWY_TEXTURE_MODELS.put("gravel", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_gravel"));
        SNOWY_TEXTURE_MODELS.put("spruce_planks", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_spruce_planks"));
        SNOWY_TEXTURE_MODELS.put("blue_ice", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_blue_ice"));
        SNOWY_TEXTURE_MODELS.put("sand", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_sand"));
        SNOWY_TEXTURE_MODELS.put("pumpkin", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_pumpkin"));
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        for (ResourceLocation modelLoc : SNOWY_TEXTURE_MODELS.values()) {
            event.register(new ModelResourceLocation(modelLoc, ""));
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();

        Map<String, BakedModel> snowyModels = new HashMap<>();
        for (Map.Entry<String, ResourceLocation> entry : SNOWY_TEXTURE_MODELS.entrySet()) {
            BakedModel snowyModel = modelRegistry.get(new ModelResourceLocation(entry.getValue(), ""));
            if (snowyModel != null) {
                snowyModels.put(entry.getKey(), snowyModel);
            }
        }

        for (Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ModelResourceLocation mrl = entry.getKey();

            if (mrl.id().getNamespace().equals("minecraft")) {
                for (Block block : OVERLAY_BLOCKS) {
                    String blockName = getBlockName(block);
                    if (mrl.id().getPath().equals(blockName)) {
                        BakedModel existingModel = entry.getValue();
                        BakedModel snowyModel = snowyModels.get(blockName);

                        if (snowyModel != null && !(existingModel instanceof SnowOverlayBakedModel)) {
                            SnowOverlayBakedModel wrappedModel = new SnowOverlayBakedModel(existingModel, snowyModel);
                            modelRegistry.put(mrl, wrappedModel);
                        }
                        break;
                    }
                }
            }
        }
    }

    private static String getBlockName(Block block) {
        return net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block).getPath();
    }
}