package jarista.tui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the TUI model against Jarista's own self-hosted feature graph.
 */
class GraphModelTest {

    private static GraphModel model;

    @BeforeAll
    static void scan() {
        model = new GraphModel("jarista.project");
        model.scan();
    }

    /** The L1 goal, sole child of the L0 vision root. */
    private static NodeView goal() {
        return model.root().children().stream()
                .filter(c -> "JaristaGoal".equals(c.name()))
                .findFirst().orElseThrow();
    }

    @Test
    void rootIsJaristaVision() {
        assertNotNull(model.root());
        assertEquals("JaristaVision", model.root().name());
        assertEquals(0, model.root().specLevel());
        assertFalse(model.root().isPiece());
    }

    @Test
    void goalIsSoleChildOfRoot() {
        assertEquals(1, model.root().children().size());
        assertEquals("JaristaGoal", goal().name());
        assertEquals(1, goal().specLevel());
    }

    @Test
    void sevenEpicsUnderGoal() {
        long epics = goal().children().stream()
                .filter(c -> !c.isPiece() && c.specLevel() == 2)
                .count();
        assertEquals(11, epics, "Expected 11 L2 epics");
    }

    @Test
    void coreTypeSystemHasFiveFeatures() {
        NodeView cts = goal().children().stream()
                .filter(c -> "CoreTypeSystem".equals(c.name()))
                .findFirst().orElseThrow();

        long features = cts.children().stream()
                .filter(c -> !c.isPiece() && c.specLevel() == 3)
                .count();
        assertEquals(5, features, "CoreTypeSystem has 5 L3 features");
    }

    @Test
    void piecesAttachToSpecs() {
        // SpecSkeleton should have ImplSpecSkeleton as a child piece
        NodeView cts = goal().children().stream()
                .filter(c -> "CoreTypeSystem".equals(c.name()))
                .findFirst().orElseThrow();
        NodeView specSkel = cts.children().stream()
                .filter(c -> "SpecSkeleton".equals(c.name()))
                .findFirst().orElseThrow();

        long pieces = specSkel.children().stream()
                .filter(NodeView::isPiece)
                .count();
        assertEquals(1, pieces, "SpecSkeleton has 1 piece");
        assertEquals("ImplSpecSkeleton", specSkel.children().stream()
                .filter(NodeView::isPiece).findFirst().orElseThrow().name());
    }

    @Test
    void totalNodeCount() {
        assertEquals(80, model.totalNodes(), "72 specs + 8 pieces = 80");
    }

    @Test
    void noViolations() {
        assertTrue(model.violations().isEmpty());
    }

    @Test
    void dependenciesPopulated() {
        // BuildIntegration should list CoreTypeSystem and GraphValidation as deps
        NodeView bi = goal().children().stream()
                .filter(c -> "BuildIntegration".equals(c.name()))
                .findFirst().orElseThrow();
        assertTrue(bi.dependencyNames().contains("CoreTypeSystem"),
                "BuildIntegration depends on CoreTypeSystem");
        assertTrue(bi.dependencyNames().contains("GraphValidation"),
                "BuildIntegration depends on GraphValidation");
    }

    @Test
    void levelTags() {
        assertEquals("L0", model.root().levelTag());
        assertEquals("L1", goal().levelTag());

        NodeView l2 = goal().children().stream()
                .filter(c -> c.specLevel() == 2).findFirst().orElseThrow();
        assertEquals("L2", l2.levelTag());
    }
}
