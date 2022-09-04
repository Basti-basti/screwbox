package de.suzufa.screwbox.core.graphics.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Frame;

class ImageUtilTest {

    private static final Image SOME_IMAGE = Frame.fromFile("tile.bmp").image();

    @Test
    void makeColorTransparent_colorNotPresent_doenstMakeAnythingTransparent() throws Exception {
        Image result = ImageUtil.makeColorTransparent(SOME_IMAGE, Color.RED);

        assertThat(countTransparentPixels(result)).isZero();
    }

    @Test
    void makeColorTransparent_colorPresent_makesMatchingPixelsTransparent() throws Exception {
        Image result = ImageUtil.makeColorTransparent(SOME_IMAGE, Color.rgb(233, 202, 177));

        assertThat(countTransparentPixels(result)).isEqualTo(2);
    }

    @Test
    void scale_sizeTooSmall_throwsException() throws IOException {
        assertThatThrownBy(() -> ImageUtil.scale(SOME_IMAGE, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scaled image is size is invalid");
    }

    @Test
    void scale_sizeValid_returnsNewImage() throws IOException {
        Image result = ImageUtil.scale(SOME_IMAGE, 4);

        assertThat(result.getWidth(null)).isEqualTo(64);
        assertThat(result.getHeight(null)).isEqualTo(64);
    }

    private int countTransparentPixels(Image result) {
        BufferedImage analyzableImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        analyzableImage.getGraphics().drawImage(result, 0, 0, null);

        int transparentPixelCount = 0;
        for (int x = 0; x < analyzableImage.getWidth(); x++) {
            for (int y = 0; y < analyzableImage.getHeight(); y++) {
                int pixel = analyzableImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha == 0) {
                    transparentPixelCount++;
                }
            }
        }
        return transparentPixelCount;
    }
}