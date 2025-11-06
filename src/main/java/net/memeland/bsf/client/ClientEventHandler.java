package net.memeland.bsf.client;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.client.model.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = BetterSnowyFeaturesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final ResourceLocation CUSTOM_SNOWY_GRASS_MODEL =
            ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_grass_block");
    private static final ResourceLocation CUSTOM_SNOWY_DIRT_MODEL =
            ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_dirt");

    private static final Map<String, ResourceLocation> SNOWY_FOLIAGE_MODELS = new HashMap<>();
    private static final Map<String, ResourceLocation> SNOWY_VINE_MODELS = new HashMap<>();
    private static final Map<String, ResourceLocation> SNOWY_PLANT_TEXTURE_MODELS = new HashMap<>();
    private static final Map<String, Map<Integer, ResourceLocation>> SNOWY_PLANT_VARIANT_MODELS = new HashMap<>();
    private static final Map<String, Map<String, ResourceLocation>> SNOWY_TALL_PLANT_TEXTURE_MODELS = new HashMap<>();
    private static final Map<String, Map<String, Map<Integer, ResourceLocation>>> SNOWY_TALL_PLANT_VARIANT_MODELS = new HashMap<>();

    static {
        SNOWY_FOLIAGE_MODELS.put("spruce_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_spruce_leaves"));
        SNOWY_FOLIAGE_MODELS.put("oak_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_oak_leaves"));
        SNOWY_FOLIAGE_MODELS.put("birch_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_birch_leaves"));
        SNOWY_FOLIAGE_MODELS.put("jungle_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_jungle_leaves"));
        SNOWY_FOLIAGE_MODELS.put("acacia_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_acacia_leaves"));
        SNOWY_FOLIAGE_MODELS.put("dark_oak_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_dark_oak_leaves"));
        SNOWY_FOLIAGE_MODELS.put("mangrove_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_mangrove_leaves"));
        SNOWY_FOLIAGE_MODELS.put("cherry_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_cherry_leaves"));
        SNOWY_FOLIAGE_MODELS.put("azalea_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_azalea_leaves"));
        SNOWY_FOLIAGE_MODELS.put("flowering_azalea_leaves", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_flowering_azalea_leaves"));

        SNOWY_VINE_MODELS.put("north", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine"));
        SNOWY_VINE_MODELS.put("south", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_n"));
        SNOWY_VINE_MODELS.put("east", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_w"));
        SNOWY_VINE_MODELS.put("west", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_e"));
        SNOWY_VINE_MODELS.put("up", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_u"));

        setupPlantModels("short_grass");
        setupPlantModels("fern");

        setupTallPlantModels("tall_grass");
        setupTallPlantModels("large_fern");
    }

    private static void setupPlantModels(String plantName) {
        SNOWY_PLANT_TEXTURE_MODELS.put(plantName,
                ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName));

        Map<Integer, ResourceLocation> variants = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            variants.put(i, ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID,
                    "block/" + plantName + "_with_snow_variant_" + i));
        }
        SNOWY_PLANT_VARIANT_MODELS.put(plantName, variants);
    }

    private static void setupTallPlantModels(String plantName) {
        Map<String, ResourceLocation> textureModels = new HashMap<>();
        textureModels.put("upper", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName + "_top"));
        textureModels.put("lower", ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName + "_bottom"));
        SNOWY_TALL_PLANT_TEXTURE_MODELS.put(plantName, textureModels);

        Map<String, Map<Integer, ResourceLocation>> allVariants = new HashMap<>();

        for (String half : new String[]{"upper", "lower"}) {
            Map<Integer, ResourceLocation> variants = new HashMap<>();
            for (int i = 0; i < 11; i++) {
                variants.put(i, ResourceLocation.fromNamespaceAndPath(BetterSnowyFeaturesMod.MOD_ID,
                        "block/" + plantName + "_" + half + "_with_snow_variant_" + i));
            }
            allVariants.put(half, variants);
        }
        SNOWY_TALL_PLANT_VARIANT_MODELS.put(plantName, allVariants);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(CUSTOM_SNOWY_GRASS_MODEL, "standalone"));
        event.register(new ModelResourceLocation(CUSTOM_SNOWY_DIRT_MODEL, "standalone"));

        for (ResourceLocation model : SNOWY_FOLIAGE_MODELS.values()) {
            event.register(new ModelResourceLocation(model, "standalone"));
        }

        for (ResourceLocation model : SNOWY_VINE_MODELS.values()) {
            event.register(new ModelResourceLocation(model, "standalone"));
        }

        for (ResourceLocation model : SNOWY_PLANT_TEXTURE_MODELS.values()) {
            event.register(new ModelResourceLocation(model, "standalone"));
        }

        for (Map<Integer, ResourceLocation> variants : SNOWY_PLANT_VARIANT_MODELS.values()) {
            for (ResourceLocation model : variants.values()) {
                event.register(new ModelResourceLocation(model, "standalone"));
            }
        }

        for (Map<String, ResourceLocation> halfModels : SNOWY_TALL_PLANT_TEXTURE_MODELS.values()) {
            for (ResourceLocation model : halfModels.values()) {
                event.register(new ModelResourceLocation(model, "standalone"));
            }
        }

        for (Map<String, Map<Integer, ResourceLocation>> halfVariants : SNOWY_TALL_PLANT_VARIANT_MODELS.values()) {
            for (Map<Integer, ResourceLocation> variants : halfVariants.values()) {
                for (ResourceLocation model : variants.values()) {
                    event.register(new ModelResourceLocation(model, "standalone"));
                }
            }
        }

        // Register snow overlay models
        SnowOverlayModelRegistry.registerAdditionalModels(event);
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();

        replaceGrassBlockModel(modelRegistry);
        replaceDirtBlockModel(modelRegistry);
        replaceFoliageModels(modelRegistry);
        replaceVineModels(modelRegistry);
        replacePlantModels(modelRegistry);
        replaceTallPlantModels(modelRegistry);

        // Replace snow overlay models
        SnowOverlayModelRegistry.replaceSnowOverlayModels(modelRegistry);
    }

    private static void replaceGrassBlockModel(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        ResourceLocation grassBlockId = ResourceLocation.fromNamespaceAndPath("minecraft", "grass_block");
        ModelResourceLocation normalGrassModel = new ModelResourceLocation(grassBlockId, "snowy=false");

        BakedModel originalNormal = modelRegistry.get(normalGrassModel);
        BakedModel customSnowy = modelRegistry.get(new ModelResourceLocation(CUSTOM_SNOWY_GRASS_MODEL, "standalone"));

        if (originalNormal != null && customSnowy != null) {
            SnowyGrassBakedModel customModel = new SnowyGrassBakedModel(originalNormal, customSnowy);
            modelRegistry.put(normalGrassModel, customModel);
        }
    }

    private static void replaceDirtBlockModel(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        ResourceLocation dirtId = ResourceLocation.fromNamespaceAndPath("minecraft", "dirt");
        ModelResourceLocation normalDirtModel = new ModelResourceLocation(dirtId, "");

        BakedModel originalNormal = modelRegistry.get(normalDirtModel);
        BakedModel customSnowy = modelRegistry.get(new ModelResourceLocation(CUSTOM_SNOWY_DIRT_MODEL, "standalone"));

        if (originalNormal != null && customSnowy != null) {
            SnowyDirtBakedModel customModel = new SnowyDirtBakedModel(originalNormal, customSnowy);

            for (Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
                ModelResourceLocation key = entry.getKey();
                if (key.id().getNamespace().equals("minecraft") && key.id().getPath().equals("dirt")) {
                    modelRegistry.put(key, customModel);
                }
            }
        }
    }

    private static void replaceFoliageModels(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        for (Map.Entry<String, ResourceLocation> entry : SNOWY_FOLIAGE_MODELS.entrySet()) {
            String blockName = entry.getKey();
            ResourceLocation customModelLocation = entry.getValue();
            replaceFoliageBlock(modelRegistry, blockName, customModelLocation);
        }
    }

    private static void replaceFoliageBlock(Map<ModelResourceLocation, BakedModel> modelRegistry, String blockName,
                                            ResourceLocation customModelLocation) {
        BakedModel customSnowy = modelRegistry.get(new ModelResourceLocation(customModelLocation, "standalone"));
        if (customSnowy == null) return;

        for (Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ModelResourceLocation mrl = entry.getKey();

            if (mrl.id().getNamespace().equals("minecraft") && mrl.id().getPath().equals(blockName)) {
                BakedModel originalModel = entry.getValue();
                SnowyFoliageBakedModel customModel = new SnowyFoliageBakedModel(originalModel, customSnowy);
                modelRegistry.put(mrl, customModel);
            }
        }
    }

    private static void replaceVineModels(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        Map<String, BakedModel> snowyVineModels = new HashMap<>();
        for (Map.Entry<String, ResourceLocation> entry : SNOWY_VINE_MODELS.entrySet()) {
            BakedModel model = modelRegistry.get(new ModelResourceLocation(entry.getValue(), "standalone"));
            if (model != null) {
                snowyVineModels.put(entry.getKey(), model);
            }
        }

        if (snowyVineModels.isEmpty()) return;

        for (Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ModelResourceLocation mrl = entry.getKey();

            if (mrl.id().getNamespace().equals("minecraft") && mrl.id().getPath().equals("vine")) {
                String variant = mrl.variant();
                BakedModel originalModel = entry.getValue();

                BakedModel snowyModel = null;
                if (variant.contains("north=true")) {
                    snowyModel = snowyVineModels.get("north");
                } else if (variant.contains("south=true")) {
                    snowyModel = snowyVineModels.get("south");
                } else if (variant.contains("east=true")) {
                    snowyModel = snowyVineModels.get("east");
                } else if (variant.contains("west=true")) {
                    snowyModel = snowyVineModels.get("west");
                } else if (variant.contains("up=true")) {
                    snowyModel = snowyVineModels.get("up");
                }

                if (snowyModel != null) {
                    SnowyFoliageBakedModel customModel = new SnowyFoliageBakedModel(originalModel, snowyModel);
                    modelRegistry.put(mrl, customModel);
                }
            }
        }
    }

    private static void replacePlantModels(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        for (String plantName : SNOWY_PLANT_TEXTURE_MODELS.keySet()) {
            ResourceLocation textureModelLoc = SNOWY_PLANT_TEXTURE_MODELS.get(plantName);
            Map<Integer, ResourceLocation> variantLocs = SNOWY_PLANT_VARIANT_MODELS.get(plantName);

            BakedModel snowyTextureModel = modelRegistry.get(new ModelResourceLocation(textureModelLoc, "standalone"));
            if (snowyTextureModel == null) continue;

            Map<Integer, BakedModel> variantModels = new HashMap<>();
            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.entrySet()) {
                BakedModel variantModel = modelRegistry.get(new ModelResourceLocation(variant.getValue(), "standalone"));
                if (variantModel != null) {
                    variantModels.put(variant.getKey(), variantModel);
                }
            }

            for (Map.Entry<ModelResourceLocation, BakedModel> modelEntry : modelRegistry.entrySet()) {
                ModelResourceLocation mrl = modelEntry.getKey();

                if (mrl.id().getNamespace().equals("minecraft") && mrl.id().getPath().equals(plantName)) {
                    BakedModel originalModel = modelEntry.getValue();
                    SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                            originalModel, snowyTextureModel, variantModels);
                    modelRegistry.put(mrl, customModel);
                }
            }
        }
    }

    private static void replaceTallPlantModels(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        for (String blockName : SNOWY_TALL_PLANT_TEXTURE_MODELS.keySet()) {
            Map<String, ResourceLocation> textureModelLocs = SNOWY_TALL_PLANT_TEXTURE_MODELS.get(blockName);
            Map<String, Map<Integer, ResourceLocation>> variantLocs = SNOWY_TALL_PLANT_VARIANT_MODELS.get(blockName);

            BakedModel upperTextureModel = modelRegistry.get(new ModelResourceLocation(textureModelLocs.get("upper"), "standalone"));
            BakedModel lowerTextureModel = modelRegistry.get(new ModelResourceLocation(textureModelLocs.get("lower"), "standalone"));

            if (upperTextureModel == null || lowerTextureModel == null) continue;

            Map<Integer, BakedModel> upperVariants = new HashMap<>();
            Map<Integer, BakedModel> lowerVariants = new HashMap<>();

            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.get("upper").entrySet()) {
                BakedModel model = modelRegistry.get(new ModelResourceLocation(variant.getValue(), "standalone"));
                if (model != null) upperVariants.put(variant.getKey(), model);
            }

            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.get("lower").entrySet()) {
                BakedModel model = modelRegistry.get(new ModelResourceLocation(variant.getValue(), "standalone"));
                if (model != null) lowerVariants.put(variant.getKey(), model);
            }

            for (Map.Entry<ModelResourceLocation, BakedModel> modelEntry : modelRegistry.entrySet()) {
                ModelResourceLocation mrl = modelEntry.getKey();

                if (mrl.id().getNamespace().equals("minecraft") && mrl.id().getPath().equals(blockName)) {
                    String variant = mrl.variant();
                    BakedModel originalModel = modelEntry.getValue();

                    if (variant.contains("half=upper")) {
                        SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                                originalModel, upperTextureModel, upperVariants);
                        modelRegistry.put(mrl, customModel);
                    } else if (variant.contains("half=lower")) {
                        SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                                originalModel, lowerTextureModel, lowerVariants);
                        modelRegistry.put(mrl, customModel);
                    }
                }
            }
        }
    }
}