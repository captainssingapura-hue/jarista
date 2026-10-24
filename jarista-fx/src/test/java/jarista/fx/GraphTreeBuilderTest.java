package jarista.fx;

import jarista.spec.SpecStatus;
import javafx.scene.control.TreeItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the FX tree model against Jarista's self-hosted feature graph.
 */
class GraphTreeBuilderTest {

    private static GraphTreeBuilder builder;

    @BeforeAll
    static void build() {
        builder = new GraphTreeBuilder("jarista.project");
        builder.build();
    }

    /** The L1 goal, sole child of the L0 vision root. */
    private static TreeItem<NodeInfo> goal() {
        return builder.rootItem().getChildren().stream()
                .filter(c -> "JaristaGoal".equals(c.getValue().name()))
                .findFirst().orElseThrow();
    }

    @Test
    void parkedAndAbandonedLeavesStayOutOfTheRollUp() {
        // shelved work must neither make a parent look active nor hold it back from DONE
        assertEquals(SpecStatus.DONE,        GraphTreeBuilder.derive(List.of(SpecStatus.DONE, SpecStatus.PARKED)));
        assertEquals(SpecStatus.DONE,        GraphTreeBuilder.derive(List.of(SpecStatus.DONE, SpecStatus.ABANDONED)));
        assertEquals(SpecStatus.PLANNED,     GraphTreeBuilder.derive(List.of(SpecStatus.PLANNED, SpecStatus.PARKED)));
        assertEquals(SpecStatus.IN_PROGRESS, GraphTreeBuilder.derive(List.of(SpecStatus.PLANNED, SpecStatus.DONE, SpecStatus.ABANDONED)));
        // nothing live left: the parent inherits, parked winning over abandoned
        assertEquals(SpecStatus.PARKED,      GraphTreeBuilder.derive(List.of(SpecStatus.PARKED, SpecStatus.ABANDONED)));
        assertEquals(SpecStatus.ABANDONED,   GraphTreeBuilder.derive(List.of(SpecStatus.ABANDONED)));
    }

    @Test
    void rootIsJaristaVision() {
        assertNotNull(builder.rootItem());
        assertEquals("JaristaVision", builder.rootItem().getValue().name());
        assertEquals(0, builder.rootItem().getValue().specLevel());
    }

    @Test
    void goalIsSoleChildOfRoot() {
        assertEquals(1, builder.rootItem().getChildren().size());
        assertEquals("JaristaGoal", goal().getValue().name());
        assertEquals(1, goal().getValue().specLevel());
    }

    @Test
    void counts() {
        assertEquals(82, builder.totalNodes());
        assertEquals(73, builder.specCount());
        assertEquals(9, builder.pieceCount());
        assertEquals(0, builder.violationCount());
    }

    @Test
    void sevenEpicsUnderGoal() {
        long epics = goal().getChildren().stream()
                .filter(c -> !c.getValue().piece() && c.getValue().specLevel() == 2)
                .count();
        assertEquals(11, epics);
    }

    @Test
    void piecesAttachToSpecs() {
        // SpecSkeleton (under CoreTypeSystem) should have ImplSpecSkeleton
        TreeItem<NodeInfo> cts = goal().getChildren().stream()
                .filter(c -> "CoreTypeSystem".equals(c.getValue().name()))
                .findFirst().orElseThrow();
        TreeItem<NodeInfo> ss = cts.getChildren().stream()
                .filter(c -> "SpecSkeleton".equals(c.getValue().name()))
                .findFirst().orElseThrow();

        assertEquals(1, ss.getChildren().size());
        assertTrue(ss.getChildren().get(0).getValue().piece());
        assertEquals("ImplSpecSkeleton", ss.getChildren().get(0).getValue().name());
    }

    @Test
    void pieceStateIsSurfacedAndSpecsHaveNone() {
        TreeItem<NodeInfo> cts = goal().getChildren().stream()
                .filter(c -> "CoreTypeSystem".equals(c.getValue().name()))
                .findFirst().orElseThrow();
        TreeItem<NodeInfo> ss = cts.getChildren().stream()
                .filter(c -> "SpecSkeleton".equals(c.getValue().name()))
                .findFirst().orElseThrow();
        NodeInfo piece = ss.getChildren().get(0).getValue();

        assertEquals(Boolean.TRUE, piece.done(), "ImplSpecSkeleton declares done()=true");
        assertEquals("✓", piece.doneMark());
        assertNotNull(piece.outOfScope());
        assertNotNull(piece.verificationTarget());

        // specs carry no piece state
        assertNull(ss.getValue().done());
        assertEquals("", ss.getValue().doneMark());
    }

    @Test
    void dependenciesPopulated() {
        TreeItem<NodeInfo> bi = goal().getChildren().stream()
                .filter(c -> "BuildIntegration".equals(c.getValue().name()))
                .findFirst().orElseThrow();
        assertTrue(bi.getValue().dependencies().contains("CoreTypeSystem"));
        assertTrue(bi.getValue().dependencies().contains("GraphValidation"));
    }

    @Test
    void childrenSorted() {
        // Specs should come before pieces, both sorted alphabetically
        var children = goal().getChildren();
        boolean seenPiece = false;
        for (var child : children) {
            if (child.getValue().piece()) {
                seenPiece = true;
            } else if (seenPiece) {
                fail("Spec appeared after piece — sort order broken");
            }
        }
    }
}
