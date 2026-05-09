package jarista.tui;

import jarista.Stateless;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A single node in the display tree — either a spec or a piece.
 */
public final class NodeView {

    private final String name;
    private final String fullName;
    private final Class<? extends Stateless> type;
    private final int specLevel;   // 1–18 for specs, 0 for pieces
    private final boolean piece;
    private final List<NodeView> children = new ArrayList<>();
    private final List<String> dependencyNames = new ArrayList<>();

    public NodeView(String name, String fullName, Class<? extends Stateless> type,
                    int specLevel, boolean piece) {
        this.name = name;
        this.fullName = fullName;
        this.type = type;
        this.specLevel = specLevel;
        this.piece = piece;
    }

    public String name()                        { return name; }
    public String fullName()                    { return fullName; }
    public Class<? extends Stateless> type()    { return type; }
    public int specLevel()                      { return specLevel; }
    public boolean isPiece()                    { return piece; }
    public List<NodeView> children()            { return children; }
    public List<String> dependencyNames()       { return dependencyNames; }

    void addChild(NodeView child)       { children.add(child); }
    void addDependency(String dep)      { dependencyNames.add(dep); }

    /** Sort children: specs first (by name), then pieces (by name). Recurse. */
    void sortChildren() {
        children.sort(Comparator
                .<NodeView, Boolean>comparing(NodeView::isPiece)
                .thenComparing(NodeView::name));
        children.forEach(NodeView::sortChildren);
    }

    /** Human-readable level tag for the right column. */
    public String levelTag() {
        if (piece) return "Piece";
        return "L" + specLevel;
    }
}
