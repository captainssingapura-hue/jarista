package jarista.web;

import hue.captains.singapura.js.homing.studio.base.composed.ComposedDoc;
import hue.captains.singapura.js.homing.studio.base.composed.RigidNodeContent;
import hue.captains.singapura.js.homing.studio.base.composed.graph.RigidNode;
import hue.captains.singapura.js.homing.studio.base.composed.text.Title;
import hue.captains.singapura.js.homing.studio.base.rigid.RigidDocV2;
import hue.captains.singapura.js.homing.tree.NodeName;

import java.util.ArrayList;
import java.util.List;

/**
 * The whole Jarista feature graph as one foldable {@link RigidDocV2} — Homing's
 * "bring your own tree" shape, which replaced the ContentTree app in 0.8.x.
 *
 * <p>The graph is walked top-down into a flat list of parent-pointer
 * {@link RigidNode}s, one per {@link GraphNode}; the framework inverts and
 * validates the tree (exactly one root, no sibling name clashes). A single
 * content provider gives every node — branch or leaf — its body from the
 * node's typed details, so unlike the old ContentTree there is no synthetic
 * "Overview" leaf: a spec's prose renders on the spec itself.
 *
 * <p>Nodes are addressed by name-path ({@code jarista/jaristagoal/...}), so
 * links survive reordering. Rendered by {@code DocTreeViewer}.
 */
public final class JaristaRigidTree {

    public static final String DOC_KEY = "feature-graph-tree";

    private JaristaRigidTree() {}

    /** One RigidNode per graph node, root first, each child after its parent. */
    private static final List<RigidNode<GraphNode>> NODES = buildNodes();

    public static final RigidDocV2 INSTANCE = RigidDocV2.fromNodes(
            DetailToDoc.uuidFor(DOC_KEY),
            "Jarista Feature Graph",
            "The self-hosted spec/piece graph as a foldable tree — expand a node to read its typed details inline.",
            "GRAPH",
            () -> NODES,
            JaristaRigidTree::contentFor);

    static List<RigidNode<GraphNode>> nodes() { return NODES; }

    private static List<RigidNode<GraphNode>> buildNodes() {
        GraphNode root = GraphNode.scanRoot(GraphNode.SCAN_PACKAGE);
        List<RigidNode<GraphNode>> all = new ArrayList<>();
        RigidNode<GraphNode> rootNode = RigidNode.root(root, new NodeName("jarista"), new Title("Jarista"));
        all.add(rootNode);
        addChildren(rootNode, root, all);
        return List.copyOf(all);
    }

    private static void addChildren(RigidNode<GraphNode> parent, GraphNode n, List<RigidNode<GraphNode>> all) {
        for (GraphNode child : n.children()) {
            RigidNode<GraphNode> rn = parent.child(child, NodeName.slug(child.name()), new Title(child.name()));
            all.add(rn);
            addChildren(rn, child, all);
        }
    }

    /** The one content provider: a node's body is its "at a glance" block plus its details. */
    static RigidNodeContent contentFor(GraphNode n) {
        return new RigidNodeContent(DetailToDoc.rigidSegments(n.meta(), n.details()));
    }

    /** Stable UUID of this doc (for viewer params). */
    static String id() { return INSTANCE.uuid().toString(); }

    // keep the ComposedDoc import meaningful for readers: the uuid helper lives there
    @SuppressWarnings("unused")
    private static final Class<?> UUID_SOURCE = ComposedDoc.class;
}
