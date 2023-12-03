package io.github.srcimon.screwbox.examples.gameoflife.grid;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.Grid.Node;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

public class GridUpdateSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);
    private static final Sheduler SHEDULER = Sheduler.withInterval(ofMillis(100));

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.ecosphere().forcedFetch(GRID_HOLDER).get(GridComponent.class);

        if (!engine.async().hasActiveTasks(gridComponent) && SHEDULER.isTick()) {
            engine.async().run(gridComponent, () -> update(gridComponent));
        }
    }

    private void update(final GridComponent gridComponent) {
        final Grid oldGrid = gridComponent.grid;
        final Grid grid = oldGrid.clearedInstance();
        for (final Node node : oldGrid.nodes()) {
            final int count = oldGrid.blockedNeighbors(node).size();
            if (oldGrid.isFree(node)) {
                if (count == 3) {
                    grid.block(node);
                }
            } else if (count == 2 || count == 3) {
                grid.block(node);
            }
        }
        gridComponent.grid = grid;
    }

}
