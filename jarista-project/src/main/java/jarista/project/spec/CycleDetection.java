package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.Set;

/** L3 — DFS-based cycle detection on the dependencies() graph. */
public class CycleDetection implements L3_Spec<GraphValidation>, LoadBearing {
    public static final CycleDetection INSTANCE = new CycleDetection();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    public Set<Stateless> dependencies() {
        return Set.of(ClasspathScanning.INSTANCE);
    }
}
