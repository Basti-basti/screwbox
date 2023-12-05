package io.github.srcimon.screwbox.core.environment;

import java.util.List;
import java.util.Optional;

public interface Environment {

    Environment addEntity(int id, Component... components);

    Environment addEntity(Component... components);

    Environment addEntity(Entity entity);

    Environment addEntities(List<Entity> entities);

    Environment addSystem(EntitySystem system);

    Environment addSystems(EntitySystem... systems);

    List<Entity> fetchAll(Archetype archetype);

    default List<Entity> fetchAllHaving(Class<? extends Component> component) {
        return fetchAll(Archetype.of(component));
    }

    default List<Entity> fetchAllHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetchAll(Archetype.of(componentA, componentB));
    }

    Optional<Entity> fetch(Archetype archetype);

    default Optional<Entity> fetchHaving(Class<? extends Component> component) {
        return fetch(Archetype.of(component));
    }

    default Optional<Entity> fetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetch(Archetype archetype);

    default Entity forcedFetchHaving(Class<? extends Component> component) {
        return forcedFetch(Archetype.of(component));
    }

    default Entity forcedFetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return forcedFetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetchById(int id);

    Optional<Entity> fetchById(int id);

    Environment remove(Entity entity);

    Environment remove(List<Entity> entities);

    /**
     * Drops all current {@link Entity}s. All {@link EntitySystem}s stay untouched.
     */
    Environment clearEntities();

    Environment toggleSystem(EntitySystem entitySystem);

    void remove(Class<? extends EntitySystem> systemType);

    long entityCount();

    boolean contains(Archetype archetype);

    Environment addSystem(Entity... entities);

    boolean isSystemPresent(Class<? extends EntitySystem> type);

    /**
     * Returns all {@link Entity}s currently attached.
     */
    List<Entity> allEntities();

    /**
     * Provides a compact syntax for importing {@link Entity}s from a custom source
     * using conditions and {@link SourceImport.Converter}.
     * 
     * @see #importSource(List) for multiple sources
     */
    <T> SourceImport<T> importSource(T source);

    /**
     * Provides a compact syntax for importing {@link Entity}s from multiple custom
     * sources using conditions and {@link SourceImport.Converter}.
     * 
     * @see #importSource(Object) for single source
     */
    <T> SourceImport<T> importSource(List<T> source);

    /**
     * Returns all {@link EntitySystem}s currently attached.
     */
    List<EntitySystem> systems();

}