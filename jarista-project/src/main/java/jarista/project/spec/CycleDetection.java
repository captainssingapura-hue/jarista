package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;

import java.util.Set;

/** L3 — DFS-based cycle detection on the dependencies() graph. */
public class CycleDetection implements L3_Spec<GraphValidation> {
    public static final CycleDetection INSTANCE = new CycleDetection();

    public Set<Stateless> dependencies() {
        return Set.of(ClasspathScanning.INSTANCE);
    }
}
