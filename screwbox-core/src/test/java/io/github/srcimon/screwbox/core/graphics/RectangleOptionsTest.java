package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static org.assertj.core.api.Assertions.assertThat;

class RectangleOptionsTest {

    @Test
    void toString_returnsInformationOnTheDrawing() {
        var options = RectangleOptions.filled(Color.RED).rotation(degrees(4));

        assertThat(options).hasToString("RectangleOptions{isFilled=true, color=Color [r=255, g=0, b=0, opacity=1.0], strokeWidth=1, rotation=Rotation [4.0°]}");
    }

}
