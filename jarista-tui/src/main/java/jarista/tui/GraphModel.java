package jarista.tui;

import jarista.Stateless;
import jarista.graph.GraphScanner;
import jarista.graph.GraphValidator;
import jarista.graph.Violation;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scans packages, classifies every Stateless node, and builds a navigable tree.
 */
public final class GraphModel {

    private static final Pattern SPEC_PAT  = Pattern.compile("L(\\d+)_Spec");
    private static final Pattern PIECE_PAT = Pattern.compile("L(\\d+)_Piece");

    private final String[] packages;
    private NodeView root;
    private int totalNodes;
    private List<Violation> violations = List.of();

    public GraphModel(String... packages) {
        this.packages = packages;
    }

    // ── public API ────────────────────────────────────────────────

    public void scan() {
        List<Class<? extends Stateless>> classes = new GraphScanner(packages).scan();
        totalNodes = classes.size();
        violations = new GraphValidator().validate(classes);

        Map<Class<?>, NodeView> viewMap = new LinkedHashMap<>();
        Map<Class<?>, Class<?>> parentMap = new HashMap<>();

        for (var cls : classes) {
            int sLevel = specLevel(cls);
            int pLevel = pieceLevel(cls);
            boolean isPiece = pLevel > 0;

            var view = new NodeView(
                    cls.getSimpleName(), cls.getName(), cls,
                    isPiece ? 0 : sLevel, isPiece);

            readDependencies(cls, view);
            viewMap.put(cls, view);

            Class<?> parent = extractParent(cls);
            if (parent != null) parentMap.put(cls, parent);
        }

        // Link children → parents
        for (var entry : parentMap.entrySet()) {
            NodeView child  = viewMap.get(entry.getKey());
            NodeView parent = viewMap.get(entry.getValue());
            if (child != null && parent != null) {
                parent.addChild(child);
            }
        }

        // Root = the single L1 spec with no parent
        root = viewMap.values().stream()
                .filter(v -> !v.isPiece() && v.specLevel() == 1)
                .findFirst()
                .orElse(null);

        if (root != null) root.sortChildren();
    }

    public NodeView root()              { return root; }
    public int totalNodes()             { return totalNodes; }
    public List<Violation> violations() { return violations; }

    // ── reflection helpers ────────────────────────────────────────

    private static int specLevel(Class<?> cls) {
        return levelOf(cls, SPEC_PAT);
    }

    private static int pieceLevel(Class<?> cls) {
        return levelOf(cls, PIECE_PAT);
    }

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

    /** Extract the generic parent type — works for both specs and pieces. */
    private static Class<?> extractParent(Class<?> cls) {
        for (Type iface : cls.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt) {
                Type raw = pt.getRawType();
                if (raw instanceof Class<?> r) {
                    String name = r.getSimpleName();
                    if (name.matches("L\\d+_(Spec|Piece)")) {
                        Type[] args = pt.getActualTypeArguments();
                        if (args.length > 0 && args[0] instanceof Class<?> parent) {
                            return parent;
                        }
                    }
                }
            }
        }
        return null;
    }

    /** Read dependencies() from the INSTANCE singleton via reflection. */
    private static void readDependencies(Class<?> cls, NodeView view) {
        try {
            Field f = cls.getField("INSTANCE");
            Object instance = f.get(null);
            Method deps = instance.getClass().getMethod("dependencies");
            @SuppressWarnings("unchecked")
            Set<Stateless> neighbors = (Set<Stateless>) deps.invoke(instance);
            for (Stateless n : neighbors) {
                view.addDependency(n.getClass().getSimpleName());
            }
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            // node has no INSTANCE or no dependencies() — that's fine
        } catch (Exception e) {
            // reflection failure — skip silently
        }
    }
}
