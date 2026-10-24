package jarista.web;

import jarista.Stateless;
import jarista.detail.DetailRole;
import jarista.detail.RoledDetail;
import jarista.graph.GraphScanner;
import jarista.piece.PieceBase;
import jarista.spec.LoadBearing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A neutral, framework-agnostic node in Jarista's feature graph, plus the
 * single reflective scan that builds the tree. Both the Homing ContentTree
 * adapter ({@link JaristaTree}) and the Homing catalogue mirror consume this,
 * so the reflection lives in exactly one place.
 *
 * <p>The reflection mirrors the JavaFX/TUI viewers: spec/piece level from the
 * {@code L(\d+)_(Spec|Piece)} interface name, parent from the first generic
 * type argument, {@code INSTANCE} for details/status/dependencies.
 */
public final class GraphNode {

    /** The package holding Jarista's self-hosted feature graph. */
    public static final String SCAN_PACKAGE = "jarista.project";

    private static final Pattern SPEC_PAT  = Pattern.compile("L(\\d+)_Spec");
    private static final Pattern PIECE_PAT = Pattern.compile("L(\\d+)_Piece");

    private final Class<?> cls;
    private final boolean piece;
    private final int level;
    private final String status;                 // LoadBearing status name, or null
    private final List<RoledDetail> details;
    private final List<String> deps;
    private final Boolean done;                  // pieces: done(); null for specs
    private final List<String> outOfScope;       // pieces: what not to touch
    private final String verificationTarget;     // pieces: how to prove it; "" if none
    private final List<GraphNode> children = new ArrayList<>();

    private GraphNode(Class<?> cls) {
        this.cls = cls;
        int pl = levelOf(cls, PIECE_PAT);
        this.piece = pl > 0;
        this.level = piece ? pl : levelOf(cls, SPEC_PAT);
        Stateless inst = instanceOf(cls);
        this.details = inst == null ? List.of() : inst.details();
        this.status = (inst instanceof LoadBearing lb) ? lb.status().name() : null;
        this.deps = dependenciesOf(inst);
        PieceBase pb = (piece && inst instanceof PieceBase p) ? p : null;
        this.done = pb == null ? null : pb.done();
        this.outOfScope = pb == null ? List.of() : pb.outOfScope();
        this.verificationTarget = pb == null ? "" : pb.verificationTarget();
    }

    // ── scan ─────────────────────────────────────────────────────

    /** Scan a package and return the L0 root node with children linked and sorted. */
    public static GraphNode scanRoot(String pkg) {
        List<Class<? extends Stateless>> classes = new GraphScanner(pkg).scan();
        Map<Class<?>, GraphNode> nodes = new LinkedHashMap<>();
        for (var cls : classes) nodes.put(cls, new GraphNode(cls));

        GraphNode root = null;
        for (GraphNode n : nodes.values()) {
            Class<?> parent = extractParent(n.cls);
            GraphNode parentNode = parent == null ? null : nodes.get(parent);
            if (parentNode != null) {
                parentNode.children.add(n);
            } else if (!n.piece && n.level == 0) {
                root = n;
            }
        }
        if (root == null) {
            throw new IllegalStateException(
                    "No L0 root spec (specLevel 0, no parent) found in " + pkg);
        }
        root.sortRecursive();
        return root;
    }

    private void sortRecursive() {
        children.sort(Comparator
                .<GraphNode, Boolean>comparing(x -> x.piece)   // specs before pieces
                .thenComparing(GraphNode::name));
        children.forEach(GraphNode::sortRecursive);
    }

    // ── accessors ────────────────────────────────────────────────

    public boolean piece()               { return piece; }
    public int level()                   { return level; }
    public String status()               { return status; }
    public List<RoledDetail> details()   { return details; }
    public List<String> deps()           { return deps; }
    public List<GraphNode> children()    { return children; }
    public boolean hasChildren()         { return !children.isEmpty(); }
    public Boolean done()                { return done; }
    public List<String> outOfScope()     { return outOfScope; }
    public String verificationTarget()   { return verificationTarget; }

    public String name()     { return cls.getSimpleName(); }
    public String fqn()      { return cls.getName(); }
    public String levelTag() { return piece ? "Piece" : "L" + level; }

    public String badge() {
        if (piece && done != null) return levelTag() + " · " + (done ? "DONE" : "OPEN");
        return status == null ? levelTag() : levelTag() + " · " + status;
    }

    public String icon() {
        if (piece) return "🔧";
        return switch (level) {
            case 0 -> "★";
            case 1 -> "◉";
            case 2 -> "◆";
            default -> "○";
        };
    }

    /** First line of the DESCRIPTION detail (falls back to the name). */
    public String summary() {
        for (RoledDetail rd : details) {
            if (rd.role() == DetailRole.DESCRIPTION
                    && rd.detail() instanceof jarista.detail.Detail.Text(var content)) {
                String first = content.strip().lines().findFirst().orElse("").strip();
                if (!first.isBlank()) return first;
            }
        }
        return name();
    }

    /** Leading "at a glance" markdown: type, status, dependencies. */
    public String meta() {
        StringBuilder sb = new StringBuilder();
        sb.append("**Type:** ").append(piece ? "Piece (L" + level + ")" : "Spec (L" + level + ")");
        if (status != null) sb.append("  \n**Status:** ").append(status);
        if (!deps.isEmpty()) sb.append("  \n**Depends on:** ").append(String.join(", ", deps));
        if (piece) {
            sb.append("  \n**Done:** ").append(Boolean.TRUE.equals(done) ? "yes ✓" : "no ○");
            sb.append("  \n**Verify:** ").append(verificationTarget.isBlank()
                    ? "_(no automated target — rely on EXPECTATION details)_"
                    : "`" + verificationTarget + "`");
            if (!outOfScope.isEmpty()) {
                sb.append("  \n**Out of scope:** ").append(String.join("; ", outOfScope));
            }
        }
        return sb.toString();
    }

    /** URL/path-safe slug of the node's simple name. */
    public String slug() {
        return name().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    // ── reflection helpers ───────────────────────────────────────

    private static int levelOf(Class<?> cls, Pattern pat) {
        for (Type iface : cls.getGenericInterfaces()) {
            Type raw = iface instanceof ParameterizedType pt ? pt.getRawType() : iface;
            if (raw instanceof Class<?> r) {
                Matcher m = pat.matcher(r.getSimpleName());
                if (m.matches()) return Integer.parseInt(m.group(1));
            }
        }
        return 0;
    }

    private static Class<?> extractParent(Class<?> cls) {
        for (Type iface : cls.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt
                    && pt.getRawType() instanceof Class<?> r
                    && r.getSimpleName().matches("L\\d+_(Spec|Piece)")) {
                Type[] args = pt.getActualTypeArguments();
                if (args.length > 0 && args[0] instanceof Class<?> parent) return parent;
            }
        }
        return null;
    }

    private static Stateless instanceOf(Class<?> cls) {
        try {
            Field f = cls.getField("INSTANCE");
            return (Stateless) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> dependenciesOf(Stateless instance) {
        if (instance == null) return List.of();
        try {
            Method deps = instance.getClass().getMethod("dependencies");
            Set<Stateless> neighbors = (Set<Stateless>) deps.invoke(instance);
            return neighbors.stream()
                    .map(x -> x.getClass().getSimpleName())
                    .sorted()
                    .toList();
        } catch (NoSuchMethodException e) {
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}
