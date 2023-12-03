package io.github.srcimon.screwbox.examples.platformer.props;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.SourceImport.Converter;
import io.github.srcimon.screwbox.core.ecosphere.components.ColliderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.MovableComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class Box implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/props/box.json");

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new PhysicsBodyComponent(),
                new MovableComponent(),
                new TransformComponent(object.bounds()),
                new ColliderComponent(500, Percent.min()));
    }

}
