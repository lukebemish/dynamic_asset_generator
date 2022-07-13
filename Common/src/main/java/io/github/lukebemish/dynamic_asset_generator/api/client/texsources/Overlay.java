package io.github.lukebemish.dynamic_asset_generator.api.client.texsources;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.lukebemish.dynamic_asset_generator.DynamicAssetGenerator;
import io.github.lukebemish.dynamic_asset_generator.client.NativeImageHelper;
import io.github.lukebemish.dynamic_asset_generator.api.client.ITexSource;
import io.github.lukebemish.dynamic_asset_generator.client.palette.ColorHolder;
import io.github.lukebemish.dynamic_asset_generator.client.util.SafeImageExtraction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record Overlay(List<ITexSource> inputs) implements ITexSource {
    public static final Codec<Overlay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITexSource.TEXSOURCE_CODEC.listOf().fieldOf("inputs").forGetter(Overlay::inputs)
    ).apply(instance, Overlay::new));

    @Override
    public Codec<? extends ITexSource> codec() {
        return CODEC;
    }

    @Override
    public Supplier<NativeImage> getSupplier() throws JsonSyntaxException {
        List<Supplier<NativeImage>> inputs = new ArrayList<>();
        for (ITexSource o : this.inputs()) {
            inputs.add(o.getSupplier());
        }
        return () -> {
            int maxX = 0;
            int maxY = 0;
            List<NativeImage> images = inputs.stream().map(Supplier::get).toList();
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i)==null) {
                    DynamicAssetGenerator.LOGGER.error("Texture given was nonexistent...\n{}",this.inputs().get(i).toString());
                    return null;
                }
            }
            for (NativeImage image : images) {
                if (image.getWidth() > maxX) {
                    maxX = image.getWidth();
                    maxY = image.getHeight();
                }
            }
            try (MultiCloser ignored = new MultiCloser(images)) {
                NativeImage output = NativeImageHelper.of(NativeImage.Format.RGBA, maxX, maxY, false);
                NativeImage base = images.get(0);
                int xs = 1;
                int ys = 1;
                if (base.getWidth() / (base.getHeight() * 1.0) <= maxX / (maxY * 1.0)) {
                    xs = maxX / base.getWidth();
                    ys = maxY / base.getWidth();
                } else {
                    xs = maxX / base.getHeight();
                    ys = maxY / base.getHeight();
                }
                for (int x = 0; x < maxX; x++) {
                    for (int y = 0; y < maxY; y++) {
                        output.setPixelRGBA(x, y, SafeImageExtraction.get(base, x / xs, y / ys));
                    }
                }
                if (images.size() >= 2) {
                    for (int i = 1; i < images.size(); i++) {
                        NativeImage image = images.get(i);
                        if (image.getWidth() / (image.getHeight() * 1.0) <= maxX / (maxY * 1.0)) {
                            xs = maxX / image.getWidth();
                            ys = maxY / image.getWidth();
                        } else {
                            xs = maxX / image.getHeight();
                            ys = maxY / image.getHeight();
                        }
                        for (int x = 0; x < maxX; x++) {
                            for (int y = 0; y < maxY; y++) {
                                ColorHolder input = ColorHolder.fromColorInt(SafeImageExtraction.get(output, x, y));
                                ColorHolder top = ColorHolder.fromColorInt(SafeImageExtraction.get(image, x / xs, y / ys));
                                ColorHolder outColor = ColorHolder.alphaBlend(top, input);
                                output.setPixelRGBA(x, y, ColorHolder.toColorInt(outColor));
                            }
                        }
                    }
                }
                return output;
            }
        };
    }

    public static class MultiCloser implements AutoCloseable {
        private final List<? extends AutoCloseable> toClose;
        public MultiCloser(List<? extends AutoCloseable> toClose) {
            this.toClose = toClose;
        }

        @Override
        public void close() {
            for (AutoCloseable c : toClose) {
                try {
                    c.close();
                } catch (Exception e) {
                    DynamicAssetGenerator.LOGGER.error("Exception while closing resources:\n",e);
                }
            }
        }
    }
}