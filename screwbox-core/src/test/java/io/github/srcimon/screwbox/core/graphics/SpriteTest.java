package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;
import static io.github.srcimon.screwbox.core.graphics.Dimension.square;
import static io.github.srcimon.screwbox.core.graphics.Offset.origin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpriteTest {

    @Test
    void invisible_returnsInvisibleSprite() {
        Sprite sprite = Sprite.invisible();

        assertThat(sprite.size()).isEqualTo(Dimension.of(1, 1));
        assertThat(sprite.singleFrame().colorAt(Offset.at(0, 0))).isEqualTo(io.github.srcimon.screwbox.core.graphics.Color.TRANSPARENT);
        assertThat(sprite.duration()).isEqualTo(Duration.none());
    }

    @Test
    void newInstance_noFrames_throwsException() {
        List<Frame> noFrames = Collections.emptyList();

        assertThatThrownBy(() -> new Sprite(noFrames))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("can not create Sprite without frames");
    }

    @Test
    void frameCount_returnsCountOfFrames() {
        Frame frame = Sprite.invisible().singleFrame();

        Sprite sprite = new Sprite(List.of(frame, frame));

        assertThat(sprite.frameCount()).isEqualTo(2);
    }

    @Test
    void fromFile_imageFound_returnsSpriteFromFile() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        assertThat(sprite.size()).isEqualTo(Dimension.of(16, 16));
    }

    @Test
    void assetFromFile_imageFound_returnsSpriteAssetFromFile() {
        Asset<Sprite> sprite = Sprite.assetFromFile("tile.bmp");

        assertThat(sprite.get().size()).isEqualTo(Dimension.of(16, 16));
    }

    @Test
    void fromFile_fileNotFound_throwsEception() {
        assertThatThrownBy(() -> Sprite.fromFile("unknown.bmp"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file not found: unknown.bmp");
    }

    @Test
    void fromFile_notAnImage_throwsEception() {
        assertThatThrownBy(() -> Sprite.fromFile("test.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("image cannot be read: test.txt");
    }

    @Test
    void multipleFromFile_dimensionLargerThanSource_emptyResult() {
        Dimension tooLarge = Dimension.of(20, 20);

        assertThat(Sprite.multipleFromFile("tile.bmp", tooLarge)).isEmpty();
    }

    @Test
    void multipleFromFile_dimensionFitsSource_returnsSubImages() {
        Dimension dimension = Dimension.of(4, 2);

        assertThat(Sprite.multipleFromFile("tile.bmp", dimension))
                .hasSize(32)
                .allMatch(s -> s.size().equals(dimension));
    }

    @Test
    void multipleFromFile_withPadding_returnsSubImages() {
        Dimension dimension = Dimension.of(4, 2);

        assertThat(Sprite.multipleFromFile("tile.bmp", dimension, 4))
                .hasSize(6)
                .allMatch(s -> s.size().equals(dimension));
    }

    @Test
    void singleFrame_moreThanOneFrame_exception() {
        Sprite first = Sprite.fromFile("tile.bmp");
        var frames = List.of(first.singleFrame(), first.singleFrame());
        var spriteWithMultipleFrames = new Sprite(frames);

        assertThatThrownBy(spriteWithMultipleFrames::singleFrame)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The sprite has more than one frame.");
    }

    @Test
    void singleFrame_oneFrame_returnsFrame() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        io.github.srcimon.screwbox.core.graphics.Frame frame = sprite.singleFrame();

        assertThat(frame.size()).isEqualTo(square(16));
    }

    @Test
    void getFrame_frameDoesntExist_exception() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        assertThatThrownBy(() -> sprite.getFrame(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot return frame nr 4, because sprite has only 1 frame(s).");
    }

    @Test
    void getFrame_invalidNumber_exception() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        assertThatThrownBy(() -> sprite.getFrame(-4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("-4 is an invalid frame number");
    }

    @Test
    void replaceColor_doenstReplacesColorOfOldFrames() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        io.github.srcimon.screwbox.core.graphics.Color oldColor = sprite.singleFrame().colorAt(origin());

        sprite.replaceColor(oldColor, BLACK);

        assertThat(oldColor).isNotEqualTo(BLACK);
    }

    @Test
    void replaceColor_replacesColorOfNewFrames() {
        Sprite sprite = Sprite.fromFile("tile.bmp");

        io.github.srcimon.screwbox.core.graphics.Color oldColor = sprite.singleFrame().colorAt(origin());

        Sprite newSprite = sprite.replaceColor(oldColor, BLACK);
        Color newColor = newSprite.singleFrame().colorAt(origin());

        assertThat(oldColor).isNotEqualTo(BLACK);
        assertThat(newColor).isEqualTo(BLACK);
    }

    @Test
    void scaled_doesntChangeOriginal() {
        Sprite original = Sprite.invisible();

        original.scaled(4);

        assertThat(original.size()).isEqualTo(square(1));
    }

    @Test
    void scaled_createsSpriteWithNewSize() {
        Sprite original = Sprite.invisible();

        Sprite scaled = original.scaled(4);

        assertThat(scaled.size()).isEqualTo(square(4));
    }

    @Test
    void image_oneImage_returnsImageOfFrame() {
        Sprite sprite = Sprite.invisible();

        Image image = sprite.image(Time.now());

        Image expectedImage = sprite.singleFrame().image();
        assertThat(image).isEqualTo(expectedImage);
    }

    @Test
    void animatedFromFile_validFileFound_returnsAnimatedSprite() {
        Sprite animatedSprite = Sprite.animatedFromFile("tile.bmp", square(2), 1, Duration.ofMillis(100));

        assertThat(animatedSprite.frameCount()).isEqualTo(25);
        assertThat(animatedSprite.duration()).isEqualTo(Duration.ofMillis(2500));
    }

    @Test
    @Timeout(5)
    void frame_returnsAllImagesOfAnAnimation() {
        Sprite animatedSprite = Sprite.animatedFromFile("tile.bmp", square(5), 1, Duration.ofMillis(50));
        List<Image> allImages = animatedSprite.allFrames().stream().map(Frame::image).toList();

        var foundImages = new HashSet<Image>();

        while (!foundImages.containsAll(allImages)) {
            foundImages.add(animatedSprite.image(Time.now()));
        }
        assertThat(foundImages).containsAll(allImages);
    }
}