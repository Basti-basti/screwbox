package de.suzufa.screwbox.core.entities.systems;

import static de.suzufa.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.PathfindingBlockingComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.test.EntityEngineExtension;
import de.suzufa.screwbox.core.utils.Timer;

@ExtendWith(EntityEngineExtension.class)
class PathfindingGridCreationSystemTest {

    @Test
    void update_noWorldBounds_throwsException(DefaultEntities entities) {
        Timer timer = Timer.withInterval(Duration.ofMillis(200));
        entities.add(new PathfindingGridCreationSystem(16, timer));

        assertThatThrownBy(() -> entities.update())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("didn't find exactly one entity matching Archetype");
    }

    @Test
    void update_noGridPresent_updatesPathfindingGrid(DefaultEntities entities, Physics physics) {
        Timer timer = Timer.withInterval(Duration.ofMillis(200));
        var worldBounds = new Entity()
                .add(new WorldBoundsComponent())
                .add(new TransformComponent($$(-100, -100, 200, 200)));

        var wall = new Entity()
                .add(new TransformComponent($$(0, 0, 100, 100)))
                .add(new PathfindingBlockingComponent());

        var air = new Entity()
                .add(new TransformComponent($$(-100, -100, 100, 100)));

        entities
                .add(new PathfindingGridCreationSystem(100, timer))
                .add(wall)
                .add(air)
                .add(worldBounds);

        entities.update();

        var gridCaptor = ArgumentCaptor.forClass(Grid.class);
        verify(physics).updatePathfindingGrid(gridCaptor.capture());

        Grid grid = gridCaptor.getValue();
        assertThat(grid.isFree(0, 0)).isTrue();
        assertThat(grid.isFree(0, 1)).isTrue();
        assertThat(grid.isFree(1, 0)).isTrue();
        assertThat(grid.isFree(1, 1)).isFalse();
    }

    @Test
    void update_invalidGridSize_throwsException(DefaultEntities entityEngine) {
        Timer timer = Timer.withInterval(Duration.ofMillis(200));
        var worldBounds = new Entity()
                .add(new WorldBoundsComponent())
                .add(new TransformComponent($$(-100, -100, 200, 200)));

        entityEngine.add(new PathfindingGridCreationSystem(16, timer)).add(worldBounds);

        assertThatThrownBy(() -> entityEngine.update())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Area origin x should be dividable by grid size.");
    }
}
