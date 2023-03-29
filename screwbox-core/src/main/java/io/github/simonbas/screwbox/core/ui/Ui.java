package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.graphics.Screen;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Create ingame menues and show loading animation.
 */
public interface Ui {

    Ui openMenu(UiMenu menu);

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

    Ui closeMenu();

    Optional<UiMenu> currentMenu();

    /**
     * Customizes the visual style of the loading animation.
     *
     * @see #showLoadingAnimationForCurrentFrame()
     */
    Ui customizeLoadingAnimation(Consumer<Screen> loadingAnimation);

    /**
     * Activates the rendering of the loading animation on the current frame.
     *
     * @see #customizeLoadingAnimation(Consumer)
     */
    void showLoadingAnimationForCurrentFrame();
}
