package de.suzufa.screwbox.playground.debo.tiles;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.StaticMarkerComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.tiled.Tile;

public class OneWayGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new SpriteComponent(tile.sprite(), tile.layer().order()),
                new StaticMarkerComponent(),
                new TransformComponent(tile.renderBounds()),
                new ColliderComponent(500, Percent.min(), true));
    }

}
