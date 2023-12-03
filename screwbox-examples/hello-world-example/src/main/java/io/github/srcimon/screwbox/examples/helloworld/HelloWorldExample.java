package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

import static io.github.srcimon.screwbox.core.graphics.Color.random;
import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

public class HelloWorldExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World Example");

        screwBox.ecosphere().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), "HELLO WORLD!", defaultFont(random()), 2);
        });

        screwBox.start();
    }
}
