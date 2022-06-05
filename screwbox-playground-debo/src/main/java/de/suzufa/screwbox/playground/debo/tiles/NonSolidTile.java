package de.suzufa.screwbox.playground.debo.tiles;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.resources.EntityConverter;
import de.suzufa.screwbox.tiled.Tile;

public class NonSolidTile implements EntityConverter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new SpriteComponent(tile.sprite(), tile.layer().order(), tile.layer().opacity()),
                new TransformComponent(tile.renderBounds()));
    }

}
