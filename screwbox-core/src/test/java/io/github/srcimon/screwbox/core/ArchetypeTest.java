package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.components.ForwardSignalComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchetypeTest {

    @Test
    void equals_identicalClasses_returnsTrue() {
        var typeA = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);
        var typeB = Archetype.of(TransformComponent.class, PhysicsBodyComponent.class);

        assertThat(typeA).isEqualTo(typeB);
    }

    @Test
    void equals_oneArchetypeIsMissingClass_returnsFalse() {
        var typeA = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);
        var typeB = Archetype.of(PhysicsBodyComponent.class);

        assertThat(typeA).isNotEqualTo(typeB);
    }

    @Test
    void matches_entityIsMissingComponent_returnsFalse() {
        var archetype = Archetype.of(TransformComponent.class);

        Entity entity = new Entity().add(new PhysicsBodyComponent());

        assertThat(archetype.matches(entity)).isFalse();
    }

    @Test
    void matches_entityHasArchetypeComponents_returnsTrue() {
        var archetype = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);

        Entity entity = new Entity()
                .add(new ForwardSignalComponent())
                .add(new PhysicsBodyComponent())
                .add(new TransformComponent(Bounds.atPosition(0, 0, 0, 0)));

        assertThat(archetype.matches(entity)).isTrue();
    }

    @Test
    void contains_doesntContainClass_isFalse() {
        var archetype = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);

        assertThat(archetype.contains(RenderComponent.class)).isFalse();
    }

    @Test
    void contains_containClass_isTrue() {
        var archetype = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);

        assertThat(archetype.contains(TransformComponent.class)).isTrue();
    }
}
