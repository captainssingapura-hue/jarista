package jarista.web;

import hue.captains.singapura.js.homing.studio.base.composed.MarkdownSegment;
import hue.captains.singapura.js.homing.studio.base.composed.graph.RigidNode;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the graph → RigidDocV2 "bring your own tree" adapter against
 * Jarista's own self-hosted feature graph.
 */
class JaristaRigidTreeTest {

    @Test
    void oneRigidNodePerGraphNode() {
        List<RigidNode<GraphNode>> nodes = JaristaRigidTree.nodes();
        // 73 specs + 9 pieces = 82 source nodes; no synthetic nodes in the rigid tree.
        assertEquals(82, nodes.size(), "expected one RigidNode per graph node");

        RigidNode<GraphNode> root = nodes.get(0);
        assertNull(root.parent(), "first node is the root");
        assertEquals(0, root.level());
        assertEquals("jarista", root.name().value());
        assertEquals("Jarista", root.title().text());
        assertEquals("JaristaVision", root.source().name());
    }

    @Test
    void levelsFollowParents() {
        for (RigidNode<GraphNode> n : JaristaRigidTree.nodes()) {
            if (n.parent() != null) {
                assertEquals(n.parent().level() + 1, n.level(),
                        "level pinned to parent+1 for " + n.name());
            }
        }
    }

    @Test
    void siblingNamesAreUnique() {
        // The framework rejects ambiguous name-paths; check it ourselves too.
        for (RigidNode<GraphNode> parent : JaristaRigidTree.nodes()) {
            Set<String> seen = new HashSet<>();
            for (RigidNode<GraphNode> n : JaristaRigidTree.nodes()) {
                if (n.parent() == parent) {
                    assertTrue(seen.add(n.name().value()),
                            "duplicate sibling name " + n.name() + " under " + parent.name());
                }
            }
        }
    }

    @Test
    void docBuildsAndValidates() {
        // toDocTreeV2() runs the normalizer: inverts parent pointers and validates
        // exactly-one-root / reachability / sibling names.
        assertNotNull(JaristaRigidTree.INSTANCE.toDocTreeV2());
        assertEquals("Jarista Feature Graph", JaristaRigidTree.INSTANCE.title());
        assertFalse(JaristaRigidTree.INSTANCE.contents().isBlank());
    }

    @Test
    void pieceBodiesShowDoneState() {
        var piece = JaristaRigidTree.nodes().stream()
                .filter(n -> n.source().piece())
                .findFirst().orElseThrow();
        var atAGlance = (MarkdownSegment) JaristaRigidTree.contentFor(piece.source()).segments().getFirst();
        assertTrue(atAGlance.body().contains("**Done:**"), "piece body states done/open");
        assertTrue(atAGlance.body().contains("**Verify:**"), "piece body states how to verify");
        assertTrue(piece.source().badge().endsWith("DONE") || piece.source().badge().endsWith("OPEN"));

        var spec = JaristaRigidTree.nodes().getFirst().source();   // the vision
        assertFalse(spec.meta().contains("**Done:**"), "specs carry no piece state");
    }

    @Test
    void everyNodeHasABody() {
        for (RigidNode<GraphNode> n : JaristaRigidTree.nodes()) {
            var content = JaristaRigidTree.contentFor(n.source());
            assertFalse(content.segments().isEmpty(), "empty body for " + n.name());
        }
    }
}
