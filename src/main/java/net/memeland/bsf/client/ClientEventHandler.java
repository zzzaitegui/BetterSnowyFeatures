package net.memeland.bsf.client;

import net.memeland.bsf.BetterSnowyFeaturesMod;
import net.memeland.bsf.client.model.SnowyDirtBakedModel;
import net.memeland.bsf.client.model.SnowyFoliageBakedModel;
import net.memeland.bsf.client.model.SnowyGrassBakedModel;
import net.memeland.bsf.client.model.SnowyPlantBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = BetterSnowyFeaturesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final ResourceLocation CUSTOM_SNOWY_GRASS_MODEL =
            new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_grass_block");
    private static final ResourceLocation CUSTOM_SNOWY_DIRT_MODEL =
            new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_dirt");

    private static final Map<String, ResourceLocation> SNOWY_FOLIAGE_MODELS = new HashMap<>();
    private static final Map<String, ResourceLocation> SNOWY_VINE_MODELS = new HashMap<>();
    private static final Map<String, ResourceLocation> SNOWY_PLANT_TEXTURE_MODELS = new HashMap<>();
    private static final Map<String, Map<Integer, ResourceLocation>> SNOWY_PLANT_VARIANT_MODELS = new HashMap<>();
    private static final Map<String, Map<String, ResourceLocation>> SNOWY_TALL_PLANT_TEXTURE_MODELS = new HashMap<>();
    private static final Map<String, Map<String, Map<Integer, ResourceLocation>>> SNOWY_TALL_PLANT_VARIANT_MODELS = new HashMap<>();

    static {
        SNOWY_FOLIAGE_MODELS.put("spruce_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_spruce_leaves"));
        SNOWY_FOLIAGE_MODELS.put("oak_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_oak_leaves"));
        SNOWY_FOLIAGE_MODELS.put("birch_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_birch_leaves"));
        SNOWY_FOLIAGE_MODELS.put("jungle_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_jungle_leaves"));
        SNOWY_FOLIAGE_MODELS.put("acacia_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_acacia_leaves"));
        SNOWY_FOLIAGE_MODELS.put("dark_oak_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_dark_oak_leaves"));
        SNOWY_FOLIAGE_MODELS.put("mangrove_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_mangrove_leaves"));
        SNOWY_FOLIAGE_MODELS.put("cherry_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_cherry_leaves"));
        SNOWY_FOLIAGE_MODELS.put("azalea_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_azalea_leaves"));
        SNOWY_FOLIAGE_MODELS.put("flowering_azalea_leaves", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_flowering_azalea_leaves"));

        SNOWY_VINE_MODELS.put("north", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine"));
        SNOWY_VINE_MODELS.put("south", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_n"));
        SNOWY_VINE_MODELS.put("east", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_w"));
        SNOWY_VINE_MODELS.put("west", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_e"));
        SNOWY_VINE_MODELS.put("up", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_vine_u"));

        setupPlantModels("grass");
        setupPlantModels("fern");

        setupTallPlantModels("tall_grass");
        setupTallPlantModels("large_fern");
    }

    private static void setupPlantModels(String plantName) {
        SNOWY_PLANT_TEXTURE_MODELS.put(plantName,
                new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName));

        Map<Integer, ResourceLocation> variants = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            variants.put(i, new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID,
                    "block/" + plantName + "_with_snow_variant_" + i));
        }
        SNOWY_PLANT_VARIANT_MODELS.put(plantName, variants);
    }

    private static void setupTallPlantModels(String plantName) {
        Map<String, ResourceLocation> textureModels = new HashMap<>();
        textureModels.put("upper", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName + "_top"));
        textureModels.put("lower", new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID, "block/snowy_" + plantName + "_bottom"));
        SNOWY_TALL_PLANT_TEXTURE_MODELS.put(plantName, textureModels);

        Map<String, Map<Integer, ResourceLocation>> allVariants = new HashMap<>();

        for (String half : new String[]{"upper", "lower"}) {
            Map<Integer, ResourceLocation> variants = new HashMap<>();
            for (int i = 0; i < 11; i++) {
                variants.put(i, new ResourceLocation(BetterSnowyFeaturesMod.MOD_ID,
                        "block/" + plantName + "_" + half + "_with_snow_variant_" + i));
            }
            allVariants.put(half, variants);
        }
        SNOWY_TALL_PLANT_VARIANT_MODELS.put(plantName, allVariants);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        event.register(CUSTOM_SNOWY_GRASS_MODEL);
        event.register(CUSTOM_SNOWY_DIRT_MODEL);

        for (ResourceLocation model : SNOWY_FOLIAGE_MODELS.values()) {
            event.register(model);
        }

        for (ResourceLocation model : SNOWY_VINE_MODELS.values()) {
            event.register(model);
        }

        for (ResourceLocation model : SNOWY_PLANT_TEXTURE_MODELS.values()) {
            event.register(model);
        }

        for (Map<Integer, ResourceLocation> variants : SNOWY_PLANT_VARIANT_MODELS.values()) {
            for (ResourceLocation model : variants.values()) {
                event.register(model);
            }
        }

        for (Map<String, ResourceLocation> halfModels : SNOWY_TALL_PLANT_TEXTURE_MODELS.values()) {
            for (ResourceLocation model : halfModels.values()) {
                event.register(model);
            }
        }

        for (Map<String, Map<Integer, ResourceLocation>> halfVariants : SNOWY_TALL_PLANT_VARIANT_MODELS.values()) {
            for (Map<Integer, ResourceLocation> variants : halfVariants.values()) {
                for (ResourceLocation model : variants.values()) {
                    event.register(model);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();

        replaceGrassBlockModel(modelRegistry);
        replaceDirtBlockModel(modelRegistry);
        replaceFoliageModels(modelRegistry);
        replaceVineModels(modelRegistry);
        replacePlantModels(modelRegistry);
        replaceTallPlantModels(modelRegistry);
    }

    private static void replaceGrassBlockModel(Map<ResourceLocation, BakedModel> modelRegistry) {
        ResourceLocation grassBlockId = new ResourceLocation("minecraft", "grass_block");
        ModelResourceLocation normalGrassModel = new ModelResourceLocation(grassBlockId, "snowy=false");

        BakedModel originalNormal = modelRegistry.get(normalGrassModel);
        BakedModel customSnowy = modelRegistry.get(CUSTOM_SNOWY_GRASS_MODEL);

        if (originalNormal != null && customSnowy != null) {
            SnowyGrassBakedModel customModel = new SnowyGrassBakedModel(originalNormal, customSnowy);
            modelRegistry.put(normalGrassModel, customModel);
        }
    }

    private static void replaceDirtBlockModel(Map<ResourceLocation, BakedModel> modelRegistry) {
        ResourceLocation dirtBlockId = new ResourceLocation("minecraft", "dirt");
        BakedModel customSnowy = modelRegistry.get(CUSTOM_SNOWY_DIRT_MODEL);

        if (customSnowy == null) return;

        for (Map.Entry<ResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ResourceLocation key = entry.getKey();

            if (key instanceof ModelResourceLocation mrl) {
                if (mrl.getNamespace().equals("minecraft") && mrl.getPath().equals("dirt")) {
                    BakedModel originalModel = entry.getValue();
                    SnowyDirtBakedModel customModel = new SnowyDirtBakedModel(originalModel, customSnowy);
                    modelRegistry.put(key, customModel);
                }
            }
        }
    }

    private static void replaceFoliageModels(Map<ResourceLocation, BakedModel> modelRegistry) {
        for (Map.Entry<String, ResourceLocation> entry : SNOWY_FOLIAGE_MODELS.entrySet()) {
            String blockName = entry.getKey();
            ResourceLocation customModelLocation = entry.getValue();
            replaceFoliageBlock(modelRegistry, blockName, customModelLocation);
        }
    }

    private static void replaceFoliageBlock(Map<ResourceLocation, BakedModel> modelRegistry, String blockName,
                                            ResourceLocation customModelLocation) {
        BakedModel customSnowy = modelRegistry.get(customModelLocation);
        if (customSnowy == null) return;

        for (Map.Entry<ResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ResourceLocation key = entry.getKey();

            if (key instanceof ModelResourceLocation mrl) {
                if (mrl.getNamespace().equals("minecraft") && mrl.getPath().equals(blockName)) {
                    BakedModel originalModel = entry.getValue();
                    SnowyFoliageBakedModel customModel = new SnowyFoliageBakedModel(originalModel, customSnowy);
                    modelRegistry.put(key, customModel);
                }
            }
        }
    }

    private static void replaceVineModels(Map<ResourceLocation, BakedModel> modelRegistry) {
        Map<String, BakedModel> snowyVineModels = new HashMap<>();
        for (Map.Entry<String, ResourceLocation> entry : SNOWY_VINE_MODELS.entrySet()) {
            BakedModel model = modelRegistry.get(entry.getValue());
            if (model != null) {
                snowyVineModels.put(entry.getKey(), model);
            }
        }

        if (snowyVineModels.isEmpty()) return;

        for (Map.Entry<ResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
            ResourceLocation key = entry.getKey();

            if (key instanceof ModelResourceLocation mrl) {
                if (mrl.getNamespace().equals("minecraft") && mrl.getPath().equals("vine")) {
                    String variant = mrl.getVariant();
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
                        modelRegistry.put(key, customModel);
                    }
                }
            }
        }
    }

    private static void replacePlantModels(Map<ResourceLocation, BakedModel> modelRegistry) {
        for (String plantName : SNOWY_PLANT_TEXTURE_MODELS.keySet()) {
            ResourceLocation textureModelLoc = SNOWY_PLANT_TEXTURE_MODELS.get(plantName);
            Map<Integer, ResourceLocation> variantLocs = SNOWY_PLANT_VARIANT_MODELS.get(plantName);

            BakedModel snowyTextureModel = modelRegistry.get(textureModelLoc);
            if (snowyTextureModel == null) continue;

            Map<Integer, BakedModel> variantModels = new HashMap<>();
            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.entrySet()) {
                BakedModel variantModel = modelRegistry.get(variant.getValue());
                if (variantModel != null) {
                    variantModels.put(variant.getKey(), variantModel);
                }
            }

            for (Map.Entry<ResourceLocation, BakedModel> modelEntry : modelRegistry.entrySet()) {
                ResourceLocation key = modelEntry.getKey();

                if (key instanceof ModelResourceLocation mrl) {
                    if (mrl.getNamespace().equals("minecraft") && mrl.getPath().equals(plantName)) {
                        BakedModel originalModel = modelEntry.getValue();
                        SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                                originalModel, snowyTextureModel, variantModels);
                        modelRegistry.put(key, customModel);
                    }
                }
            }
        }
    }

    private static void replaceTallPlantModels(Map<ResourceLocation, BakedModel> modelRegistry) {
        for (String blockName : SNOWY_TALL_PLANT_TEXTURE_MODELS.keySet()) {
            Map<String, ResourceLocation> textureModelLocs = SNOWY_TALL_PLANT_TEXTURE_MODELS.get(blockName);
            Map<String, Map<Integer, ResourceLocation>> variantLocs = SNOWY_TALL_PLANT_VARIANT_MODELS.get(blockName);

            BakedModel upperTextureModel = modelRegistry.get(textureModelLocs.get("upper"));
            BakedModel lowerTextureModel = modelRegistry.get(textureModelLocs.get("lower"));

            if (upperTextureModel == null || lowerTextureModel == null) continue;

            Map<Integer, BakedModel> upperVariants = new HashMap<>();
            Map<Integer, BakedModel> lowerVariants = new HashMap<>();

            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.get("upper").entrySet()) {
                BakedModel model = modelRegistry.get(variant.getValue());
                if (model != null) upperVariants.put(variant.getKey(), model);
            }

            for (Map.Entry<Integer, ResourceLocation> variant : variantLocs.get("lower").entrySet()) {
                BakedModel model = modelRegistry.get(variant.getValue());
                if (model != null) lowerVariants.put(variant.getKey(), model);
            }

            for (Map.Entry<ResourceLocation, BakedModel> modelEntry : modelRegistry.entrySet()) {
                ResourceLocation key = modelEntry.getKey();

                if (key instanceof ModelResourceLocation mrl) {
                    if (mrl.getNamespace().equals("minecraft") && mrl.getPath().equals(blockName)) {
                        String variant = mrl.getVariant();
                        BakedModel originalModel = modelEntry.getValue();

                        if (variant.contains("half=upper")) {
                            SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                                    originalModel, upperTextureModel, upperVariants);
                            modelRegistry.put(key, customModel);
                        } else if (variant.contains("half=lower")) {
                            SnowyPlantBakedModel customModel = new SnowyPlantBakedModel(
                                    originalModel, lowerTextureModel, lowerVariants);
                            modelRegistry.put(key, customModel);
                        }
                    }
                }
            }
        }
    }
}