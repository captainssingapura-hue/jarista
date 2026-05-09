package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Build-time graph validation: INSTANCE enforcement, cycle detection, orphan scanning.
 * The solo button. Press it — every node must prove it's real.
 */
public class GraphValidation implements L2_Spec<JaristaGoal> {
    public static final GraphValidation INSTANCE = new GraphValidation();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE);
    }
}
