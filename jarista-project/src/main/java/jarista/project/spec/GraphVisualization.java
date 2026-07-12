package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Visual graph layout (layered or force-directed) alongside the tree view. */
public class GraphVisualization implements L3_Spec<UILayer>, LoadBearing {
    public static final GraphVisualization INSTANCE = new GraphVisualization();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
