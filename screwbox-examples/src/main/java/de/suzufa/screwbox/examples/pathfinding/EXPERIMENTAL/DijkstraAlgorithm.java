package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;

public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Node> findPath(final Grid raster, final Node start, final Node end) {
        final List<Node> used = new ArrayList<>();
        used.add(start);

        while (true) {
            final List<Node> newOpen = new ArrayList<>();
            for (var use : used) {
                for (final Node neighbor : raster.findNeighbors(use)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for (final Node point : newOpen) {
                used.add(point);
                if (end.equals(point)) {
                    return used.get(used.size() - 1).backtrackPath();
                }
            }

            if (newOpen.isEmpty()) {
                return emptyList();
            }
        }
    }

}
