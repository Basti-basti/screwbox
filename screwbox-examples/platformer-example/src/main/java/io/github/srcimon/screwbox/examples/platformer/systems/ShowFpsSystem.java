package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.Order;
import io.github.srcimon.screwbox.core.ecosphere.SystemOrder;
import io.github.srcimon.screwbox.core.ecosphere.components.ColliderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.Offset;

@Order(SystemOrder.PRESENTATION_UI_FOREGROUND)
public class ShowFpsSystem implements EntitySystem {

    private static final Archetype COLLIDERS = Archetype.of(ColliderComponent.class, TransformComponent.class);
    private static final Offset TEXT_POSITION = Offset.at(50, 50);
    private static final Font FONT = new Font("Futura", 14);

    @Override
    public void update(Engine engine) {
        long colliderCount = engine.ecosphere().fetchAll(COLLIDERS).size();
        long entityCount = engine.ecosphere().entityCount();
        int fps = engine.loop().fps();
        long updateTime = engine.loop().updateDuration().milliseconds();
        String text = String.format("fps: %04d / updatetime %02d / %d entities / %d colliders",
                fps, updateTime, entityCount, colliderCount);
        engine.graphics().screen().drawText(TEXT_POSITION, text, FONT, Color.WHITE);
    }
}
