package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.utils.MacOsSupport;

import java.awt.*;
import java.io.Serial;

public class MacOsWindowFrame extends WindowFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void makeFullscreen(final GraphicsDevice graphicsDevice) {
        try {
            Thread.sleep(500); // fixes Problem when called to fast after closing a window, I am sorry!
            final var screenUtils = Class.forName("com.apple.eawt.FullScreenUtilities");
            final var canFullscreenMethod = screenUtils.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
            canFullscreenMethod.invoke(screenUtils, this, true);

            final var application = Class.forName("com.apple.eawt.Application");
            final var fullScreenMethod = application.getMethod("requestToggleFullScreen", Window.class);
            fullScreenMethod.invoke(application.getConstructor().newInstance(), this);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Please add jvm parameters to allow native fullscreen on MacOs: " + MacOsSupport.FULLSCREEN_JVM_OPTION,
                    e);
        }
    }
}
