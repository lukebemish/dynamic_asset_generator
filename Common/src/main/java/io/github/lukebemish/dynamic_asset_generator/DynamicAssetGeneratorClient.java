package io.github.lukebemish.dynamic_asset_generator;

import io.github.lukebemish.dynamic_asset_generator.client.api.DynAssetGeneratorClientAPI;
import io.github.lukebemish.dynamic_asset_generator.client.api.ForegroundTransferType;
import io.github.lukebemish.dynamic_asset_generator.client.api.PaletteExtractor;
import io.github.lukebemish.dynamic_asset_generator.client.api.json.DynamicTextureJson;
import io.github.lukebemish.dynamic_asset_generator.client.json.*;
import io.github.lukebemish.dynamic_asset_generator.client.util.IPalettePlan;
import io.github.lukebemish.dynamic_asset_generator.platform.Services;
import net.minecraft.resources.ResourceLocation;

public class DynamicAssetGeneratorClient {
    public static void init() {
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"texture"),TextureReader.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"fallback"),FallbackSource.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"combined_paletted_image"),CombinedPaletteImage.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"overlay"),Overlay.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"mask"),Mask.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"crop"),Crop.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"transform"),Transform.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"foreground_transfer"),ForegroundTransfer.CODEC);
        DynamicTextureJson.registerTexSourceReadingType(new ResourceLocation(DynamicAssetGenerator.MOD_ID,"color"),ColorSource.CODEC);
        //testing

        if (Services.PLATFORM.isDev()) {
            //IPalettePlan p = new PlannedPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/stone.png"),
            //        new ResourceLocation("minecraft", "textures/item/apple.png"), new ResourceLocation("minecraft", "textures/item/gold_ingot.png"), true, 0, true);
            //DynAssetGeneratorClientAPI.planPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/end_stone.png"), p);

            String background = "textures/block/calcite.png";
            PaletteExtractor extractor = new PaletteExtractor(new ResourceLocation("minecraft", "textures/block/stone.png"),
                    new ResourceLocation("minecraft", "textures/block/gold_ore.png"), 6, true, true, 0.2).fillHoles(true);
            IPalettePlan plan = new ForegroundTransferType(extractor, new ResourceLocation("minecraft", background),
                    true, false);
            PaletteExtractor extractor2 = new PaletteExtractor(new ResourceLocation("minecraft", "textures/block/stone.png"),
                    new ResourceLocation("minecraft", "textures/block/redstone_ore.png"), 6, true, true, 0.2).fillHoles(true);
            IPalettePlan plan2 = new ForegroundTransferType(extractor2, new ResourceLocation("minecraft", background),
                    true, false);
            PaletteExtractor extractor3 = new PaletteExtractor(new ResourceLocation("minecraft", "textures/block/stone.png"),
                    new ResourceLocation("minecraft", "textures/block/copper_ore.png"), 6, true, true, 0.2).fillHoles(true);
            IPalettePlan plan3 = new ForegroundTransferType(extractor3, new ResourceLocation("minecraft", background),
                    true, false);
            PaletteExtractor extractor4 = new PaletteExtractor(new ResourceLocation("minecraft", "textures/block/stone.png"),
                    new ResourceLocation("minecraft", "textures/block/emerald_ore.png"), 6, true, true, 0.2).fillHoles(true);
            IPalettePlan plan4 = new ForegroundTransferType(extractor4, new ResourceLocation("minecraft", background),
                    true, false);
            DynAssetGeneratorClientAPI.planPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/end_stone.png"), plan);
            DynAssetGeneratorClientAPI.planPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/cobblestone.png"), plan2);
            DynAssetGeneratorClientAPI.planPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/tuff.png"), plan3);
            DynAssetGeneratorClientAPI.planPaletteCombinedImage(new ResourceLocation("minecraft", "textures/block/netherrack.png"), plan4);

            //DynAssetGenClientPlanner.planPaletteCombinedImage(new ResourceLocation("minecraft","textures/item/gold_ingot.png"),
            //        new PlannedPaletteCombinedImage(new ResourceLocation("minecraft","textures/block/moss_block.png"), new ResourceLocation("dynamic_asset_generator","textures/empty.png"), new ResourceLocation("minecraft","textures/item/copper_ingot.png"), false, 6, true));
        }
    }
}
