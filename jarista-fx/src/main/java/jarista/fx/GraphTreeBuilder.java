package jarista.fx;

import jarista.Stateless;
import jarista.detail.RoledDetail;
import jarista.graph.GraphScanner;
import jarista.graph.GraphValidator;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;
import javafx.scene.control.TreeItem;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scans packages for Stateless nodes and builds a JavaFX TreeItem hierarchy.
 */
public final class GraphTreeBuilder {

    private static final Pattern SPEC_PAT  = Pattern.compile("L(\\d+)_Spec");
    private static final Pattern PIECE_PAT = Pattern.compile("L(\\d+)_Piece");

    private final String[] packages;
    private TreeItem<NodeInfo> rootItem;
    private int totalNodes;
    private int specCount;
    private int pieceCount;
    private int violationCount;

    public GraphTreeBuilder(String... packages) {
        this.packages = packages;
    }

    // ── public API ───────────────────────────────────────────────

    public void build() {
        List<Class<? extends Stateless>> classes = new GraphScanner(packages).scan();
        totalNodes = classes.size();
        violationCount = new GraphValidator().validate(classes).size();

        Map<Class<?>, TreeItem<NodeInfo>> itemMap = new LinkedHashMap<>();
        Map<Class<?>, Class<?>> parentMap = new HashMap<>();
        int specs = 0, pieces = 0;

        for (var cls : classes) {
            int sLevel = specLevel(cls);
            int pLevel = pieceLevel(cls);
            boolean isPiece = pLevel > 0;
            if (isPiece) pieces++; else specs++;

            Stateless instance = getInstance(cls);
            boolean isLoadBearing = instance instanceof LoadBearing;
            SpecStatus declaredStatus = (instance instanceof LoadBearing lb) ? lb.status() : null;

            var info = new NodeInfo(
                    cls.getSimpleName(), cls.getName(),
                    isPiece ? 0 : sLevel, isPiece,
                    isLoadBearing, isPiece ? null : declaredStatus,
                    readDependencies(cls), readDetails(cls));

            var item = new TreeItem<>(info);
            item.setExpanded(true);
            itemMap.put(cls, item);

            Class<?> parent = extractParent(cls);
            if (parent != null) parentMap.put(cls, parent);
        }

        specCount = specs;
        pieceCount = pieces;

        // link children → parents
        for (var entry : parentMap.entrySet()) {
            var child  = itemMap.get(entry.getKey());
            var parent = itemMap.get(entry.getValue());
            if (child != null && parent != null) {
                parent.getChildren().add(child);
            }
        }

        // derive status for organizational (non-LoadBearing) specs
        deriveOrganizationalStatus(itemMap);

        // sort: specs first (by name), then pieces (by name)
        for (var item : itemMap.values()) {
            item.getChildren().sort(Comparator
                    .<TreeItem<NodeInfo>, Boolean>comparing(i -> i.getValue().piece())
                    .thenComparing(i -> i.getValue().name()));
        }

        // root = the L1 spec with no parent
        rootItem = itemMap.values().stream()
                .filter(i -> !i.getValue().piece() && i.getValue().specLevel() == 1)
                .findFirst().orElse(null);
    }

    public TreeItem<NodeInfo> rootItem()    { return rootItem; }
    public int totalNodes()                 { return totalNodes; }
    public int specCount()                  { return specCount; }
    public int pieceCount()                 { return pieceCount; }
    public int violationCount()             { return violationCount; }

    // ── reflection helpers ───────────────────────────────────────

    private static int specLevel(Class<?> cls)  { return levelOf(cls, SPEC_PAT); }
    private static int pieceLevel(Class<?> cls) { return levelOf(cls, PIECE_PAT); }

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
            if (iface instanceof ParameterizedType pt) {
                Type raw = pt.getRawType();
                if (raw instanceof Class<?> r && r.getSimpleName().matches("L\\d+_(Spec|Piece)")) {
                    Type[] args = pt.getActualTypeArguments();
                    if (args.length > 0 && args[0] instanceof Class<?> parent) return parent;
                }
            }
        }
        return null;
    }

    private static Stateless getInstance(Class<?> cls) {
        try {
            Field f = cls.getField("INSTANCE");
            return (Stateless) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> readDependencies(Class<?> cls) {
        try {
            Stateless instance = getInstance(cls);
            if (instance == null) return List.of();
            Method deps = instance.getClass().getMethod("dependencies");
            Set<Stateless> neighbors = (Set<Stateless>) deps.invoke(instance);
            return neighbors.stream()
                    .map(n -> n.getClass().getSimpleName())
                    .sorted()
                    .toList();
        } catch (NoSuchMethodException e) {
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

    private static List<RoledDetail> readDetails(Class<?> cls) {
        try {
            Stateless instance = getInstance(cls);
            if (instance == null) return List.of();
            return instance.details();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Derives indicative status for organizational (non-LoadBearing) specs
     * by rolling up descendant leaves. Bottom-up: process deepest first.
     *
     * <p>Indicative only — software is a living organism; even DONE leaves
     * may grow new sub-features later.
     */
    private static void deriveOrganizationalStatus(Map<Class<?>, TreeItem<NodeInfo>> itemMap) {
        for (var item : itemMap.values()) {
            NodeInfo info = item.getValue();
            if (info.piece() || info.loadBearing() || info.status() != null) continue;

            SpecStatus derived = deriveFromChildren(item);
            if (derived != null) {
                // rebuild NodeInfo with derived status
                var updated = new NodeInfo(
                        info.name(), info.fullName(), info.specLevel(),
                        info.piece(), info.loadBearing(), derived,
                        info.dependencies(), info.details());
                item.setValue(updated);
            }
        }
    }

    /**
     * Collects leaf statuses from all spec descendants (skipping pieces).
     * Returns null if no leaf descendants found.
     */
    private static SpecStatus deriveFromChildren(TreeItem<NodeInfo> item) {
        var leafStatuses = new ArrayList<SpecStatus>();
        collectLeafStatuses(item, leafStatuses);
        if (leafStatuses.isEmpty()) return null;

        boolean allDone = leafStatuses.stream().allMatch(s -> s == SpecStatus.DONE);
        boolean anyActive = leafStatuses.stream().anyMatch(s -> s != SpecStatus.PLANNED);

        if (allDone) return SpecStatus.DONE;
        if (anyActive) return SpecStatus.IN_PROGRESS;
        return SpecStatus.PLANNED;
    }

    private static void collectLeafStatuses(TreeItem<NodeInfo> item, List<SpecStatus> out) {
        for (var child : item.getChildren()) {
            NodeInfo ci = child.getValue();
            if (ci.piece()) continue;
            if (ci.loadBearing() && ci.status() != null) {
                out.add(ci.status());
            } else {
                collectLeafStatuses(child, out);
            }
        }
    }
}
