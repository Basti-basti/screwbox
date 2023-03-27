package io.github.simonbas.screwbox.examples.pathfinding.scenes;

import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.core.entities.systems.*;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.utils.Timer;
import io.github.simonbas.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import io.github.simonbas.screwbox.examples.pathfinding.components.SpriteChangeComponent;
import io.github.simonbas.screwbox.examples.pathfinding.systems.EnemyMovementSystem;
import io.github.simonbas.screwbox.examples.pathfinding.systems.PlayerControlSystem;
import io.github.simonbas.screwbox.examples.pathfinding.systems.SpriteChangeSystem;
import io.github.simonbas.screwbox.tiled.GameObject;
import io.github.simonbas.screwbox.tiled.Map;
import io.github.simonbas.screwbox.tiled.Tile;

import static io.github.simonbas.screwbox.core.Bounds.atPosition;
import static io.github.simonbas.screwbox.core.Duration.ofSeconds;
import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DemoScene implements Scene {

    private static final Asset<Sprite> PLAYER_STANDING = spriteAssetFromJson("player.json", "standing");
    private static final Asset<Sprite> PLAYER_WALKING = spriteAssetFromJson("player.json", "walking");
    private static final Asset<Sprite> ENEMY_STANDING = spriteAssetFromJson("enemy.json", "standing");
    private static final Asset<Sprite> ENEMY_WALKING = spriteAssetFromJson("enemy.json", "walking");

    private final Map map;

    public DemoScene(final String name) {
        map = Map.fromJson(name);
    }

    @Override
    public void initialize(final Entities entities) {
        entities.importSource(map.tiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entities.importSource(map)
                .as(worldBounds());

        entities.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("enemy").as(enemy())
                .when("camera").as(camera());

        entities.add(new RenderSystem())
                .add(new CameraMovementSystem())
                .add(new StateSystem())
                .add(new PlayerControlSystem())
                .add(new AutoRotationSystem())
                .add(new AutomovementSystem())
                .add(new AutomovementDebugSystem())
                .add(new LogFpsSystem())
                .add(new PathfindingGridCreationSystem(16, Timer.withInterval(ofSeconds(1))))
                .add(new EnemyMovementSystem())//TODO: FIX BLACK SCREEN FOR 1 SEC ON START WHEN ADDING THIS SYSTEM
                .add(new SpriteChangeSystem())
                .add(new PhysicsSystem());
    }

    private Converter<GameObject> camera() {
        return object -> new Entity()
                .add(new TransformComponent(object.bounds()))
                .add(new CameraComponent(2.5))
                .add(new CameraMovementComponent(2, object.properties().forceInt("target")));
    }

    private Converter<GameObject> player() {
        return object -> new Entity(object.id())
                .add(new SpriteChangeComponent(PLAYER_STANDING.get(), PLAYER_WALKING.get()))
                .add(new PlayerMovementComponent())
                .add(new PhysicsBodyComponent())
                .add(new AutoRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsBodyComponent())
                .add(new AutomovementComponent(30))
                .add(new AutoRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                .add(new TransformComponent(tile.renderBounds()));
    }

    private Converter<Tile> wall() {
        return tile -> floor().convert(tile)
                .add(new PathfindingBlockingComponent())
                .add(new ColliderComponent());
    }

    private static Converter<Map> worldBounds() {
        return map -> new Entity()
                .add(new TransformComponent(map.bounds()))
                .add(new WorldBoundsComponent());
    }
}