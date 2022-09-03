package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.TileEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

public class TileDicitonary {

    private final List<Tile> tiles = new ArrayList<>();

    public TileDicitonary(final MapEntity map) {
        final Tileset tileset = SpriteLoader.loadTileset(map);
        final PropertiesCollection propertiesDictionary = loadTileProperties(map);

        int order = 0;
        for (final LayerEntity layerEntity : map.getLayers()) {
            for (int y = 0; y < layerEntity.height(); y++) {
                for (int x = 0; x < layerEntity.width(); x++) {
                    final Integer tileId = layerEntity.data().get(y * layerEntity.width() + x);
                    if (tileId != 0) {
                        final double width = map.getTilewidth();
                        final double height = map.getTileheight();
                        final double offsetX = x * width;
                        final double offsetY = y * height;
                        final Bounds bounds = Bounds.atOrigin(offsetX, offsetY, width, height);
                        final Sprite sprite = tileset.findById(tileId);
                        final Layer layer = new Layer(layerEntity, order);
                        final Properties properties = propertiesDictionary.get(tileId);
                        final var tile = new Tile(sprite, bounds, layer, properties);
                        add(tile);
                    }
                }
            }
            order++;
        }
    }

    public void add(final Tile tile) {
        tiles.add(tile);
    }

    public List<Tile> all() {
        return tiles;
    }

    public List<Tile> allFromLayer(final String name) {
        return tiles.stream()
                .filter(t -> name.equals(t.layer().name()))
                .toList();
    }

    private PropertiesCollection loadTileProperties(final MapEntity map) {
        final PropertiesCollection dictionary = new PropertiesCollection();
        for (final TilesetEntity tileset : map.getTilesets()) {
            for (final TileEntity tileEntity : tileset.getTiles()) {
                final Properties properties = new Properties(tileEntity.properties());
                dictionary.add(tileset.getFirstgid() + tileEntity.id(), properties);
            }
        }
        return dictionary;
    }

}