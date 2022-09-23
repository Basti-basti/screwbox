package de.suzufa.screwbox.core;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Grid.Node;

class GridTest {

    @Test
    void cleared_someFieldsBlocked_returnsEmptyGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        Grid grid = new Grid(area, 2);
        grid.block(1, 2);

        Grid result = grid.cleared();

        assertThat(result.isBlocked(1, 2)).isFalse();
        assertThat(result.area()).isEqualTo(area);
    }

    @Test
    void newInstance_widthNegative_throwsException() {
        assertThatThrownBy(() -> new Grid(null, 4))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Grid area must not be null");
    }

    @Test
    void newInstance_gridSizeZero_throwsException() {
        Bounds area = Bounds.max();
        assertThatThrownBy(() -> new Grid(area, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GridSize must have value above zero");
    }

    @Test
    void newInstance_invalidAreaOriginX_throwsException() {
        Bounds area = Bounds.atOrigin(1, 0, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Area origin x should be dividable by grid size.");
    }

    @Test
    void newInstance_invalidAreaOriginY_throwsException() {
        Bounds area = Bounds.atOrigin(-32, 4, 10, 10);
        assertThatThrownBy(() -> new Grid(area, 16))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Area origin y should be dividable by grid size.");
    }

    @Test
    void newInstance_validArguments_createsEmptyGrid() {
        Bounds area = Bounds.atOrigin(Vector.zero(), 400, 200);

        var grid = new Grid(area, 20, false);

        assertThat(grid.nodes())
                .hasSize(200)
                .allMatch(grid::isFree);

        assertThat(grid.width()).isEqualTo(20);
        assertThat(grid.height()).isEqualTo(10);
    }

    @Test
    void reachableNeighbors_noDiagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16, false);

        assertThat(grid.reachableNeighbors(grid.nodeAt(1, 1)))
                .hasSize(4)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(1, 0))
                .contains(grid.nodeAt(1, 2));
    }

    @Test
    void reachableNeighbors_diagonalMovement_returnsNeighbours() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.reachableNeighbors(grid.nodeAt(1, 1)))
                .hasSize(8)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(1, 0))
                .contains(grid.nodeAt(1, 2))
                .contains(grid.nodeAt(0, 0))
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(2, 0))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void reachableNeighbors_onEdge_returnsNeighboursInGrid() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);

        var grid = new Grid(area, 16);

        assertThat(grid.reachableNeighbors(grid.nodeAt(0, 0)))
                .hasSize(3)
                .contains(grid.nodeAt(0, 1))
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(1, 0));
    }

    @Test
    void backtrack_noParent_returnsNoNodes() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid(area, 16);
        Node node = grid.nodeAt(0, 0);

        assertThat(grid.backtrack(node)).isEmpty();
    }

    @Test
    void backtrack_parentPresent_returnsNodesPath() {
        Bounds area = Bounds.atOrigin(0, 0, 64, 64);
        var grid = new Grid(area, 16);
        Node first = grid.nodeAt(0, 0);

        List<Node> secondGeneration = grid.reachableNeighbors(first);
        Node second = secondGeneration.get(0);

        List<Node> thirdGeneration = grid.reachableNeighbors(second);
        Node third = thirdGeneration.get(0);

        List<Node> path = grid.backtrack(third);

        assertThat(path).containsExactlyInAnyOrder(second, third);
    }

    @Test
    void toGrid_translatesVectorToGrid() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Node node = grid.toGrid($(192, -64));

        assertThat(node).isEqualTo(grid.nodeAt(11, -2));
    }

    @Test
    void toWorld_translatesNodeFromGridToWorld() {
        Bounds area = Bounds.atOrigin(16, -32, 64, 64);
        var grid = new Grid(area, 16);

        Node node = grid.toGrid($(192, -64));
        Vector vector = grid.toWorld(node);

        assertThat(vector).isEqualTo($(200, -56));
    }

    @Test
    void blockArea_areaInGrid_blocksGridArea() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockArea($$(3, 2, 2, 3));

        assertThat(grid.isFree(0, 0)).isFalse();
        assertThat(grid.isFree(0, 1)).isFalse();
        assertThat(grid.isFree(1, 0)).isFalse();
        assertThat(grid.isFree(1, 1)).isFalse();
        assertThat(grid.isFree(2, 0)).isTrue();
        assertThat(grid.isFree(2, 1)).isTrue();
        assertThat(grid.isFree(0, 2)).isTrue();
        assertThat(grid.isFree(1, 2)).isTrue();
        assertThat(grid.isFree(2, 2)).isTrue();
    }

    @Test
    void blockedNeighbors_someNeighborsBlocked_returnsBlockedOnes() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockArea($$(3, 2, 8, 8));

        assertThat(grid.blockedNeighbors(grid.nodeAt(1, 2)))
                .hasSize(3)
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(2, 1))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void blockedNeighbors_noDiagonalSearch_returnsBlockedOnes() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4, false);

        grid.blockArea($$(3, 2, 8, 8));

        assertThat(grid.blockedNeighbors(grid.nodeAt(1, 2)))
                .hasSize(2)
                .contains(grid.nodeAt(1, 1))
                .contains(grid.nodeAt(2, 2));
    }

    @Test
    void blockAt_positionInGrid_blocksNodeAtPosition() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);

        grid.blockAt($(5, 5));

        assertThat(grid.isBlocked(1, 1)).isTrue();
        assertThat(grid.blockedCount()).isEqualTo(1);
    }

    @Test
    void freeAt_positionInGrid_freesPosition() {
        Bounds area = $$(0, 0, 12, 12);
        var grid = new Grid(area, 4);
        grid.blockArea(area);

        grid.freeAt($(5, 5));

        assertThat(grid.isFree(1, 1)).isTrue();
        assertThat(grid.blockedCount()).isEqualTo(8);
    }

}
