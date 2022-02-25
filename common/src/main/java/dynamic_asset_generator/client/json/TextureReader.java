package dynamic_asset_generator.client.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import dynamic_asset_generator.DynamicAssetGenerator;
import dynamic_asset_generator.client.api.json.ITexSource;
import dynamic_asset_generator.client.util.ImageUtils;
import net.minecraft.resources.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Supplier;

public class TextureReader implements ITexSource {
    public static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Override
    public Supplier<BufferedImage> getSupplier(String inputStr) throws JsonSyntaxException{
        LocationSource locationSource = gson.fromJson(inputStr, LocationSource.class);
        ResourceLocation rl = ResourceLocation.of(locationSource.path,':');
        ResourceLocation out_rl = new ResourceLocation(rl.getNamespace(), "textures/"+rl.getPath()+".png");
        return () -> {
            try {
                return ImageUtils.getImage(out_rl);
            } catch (IOException e) {
                DynamicAssetGenerator.LOGGER.error("Issue loading texture: {}", rl);
            }
            return null;
        };
    }

    public static class LocationSource {
        @Expose
        String source_type;
        @Expose
        public String path;
    }
}