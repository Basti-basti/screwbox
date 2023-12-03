package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Ecosphere;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundHolderComponent;
import io.github.srcimon.screwbox.examples.platformer.menues.PauseMenu;
import io.github.srcimon.screwbox.examples.platformer.systems.GetSreenshotOfGameSceneSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
        engine.window().setTitle("Platformer Example (Pause)");
    }

    @Override
    public void populate(Ecosphere ecosphere) {
        ecosphere.addSystem(new GetSreenshotOfGameSceneSystem())
                .addSystem(new RenderPauseScreenshotSystem())
                .addEntity(new Entity()
                        .add(new BackgroundHolderComponent()));
    }

}
