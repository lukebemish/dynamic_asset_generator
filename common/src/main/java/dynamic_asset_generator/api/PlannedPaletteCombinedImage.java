package dynamic_asset_generator.api;

import net.minecraft.resources.ResourceLocation;

/**
 * An object holding information for assembling a palette combined image. To assemble an image, a palette is first
 * extracted from the background. The paletted image is colored based on this palette. Then, the images are stacked,
 * with the background on the bottom and the overlay on top.
 *
 * @param background        location of the background image
 * @param overlay           location of the overlay image
 * @param paletted          location of the palette sampling image
 * @param includeBackground whether the background image should be included in the final image
 * @param extend            the minimum size to extend the palette to; set to 0 or less to disable.
 * @param stretchPaletted   whether to "stretch" the paletted image so that its maximum and minimum values line up with
 *                          the darkest and lightest values of the palette
 */
public record PlannedPaletteCombinedImage(ResourceLocation background, ResourceLocation overlay, ResourceLocation paletted, boolean includeBackground, int extend, boolean stretchPaletted) { }
