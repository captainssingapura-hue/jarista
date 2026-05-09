package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Externalized state: reference implementations of StateStore.
 * The keystone for daily usage. Without this, adoption stalls.
 */
public class StateManagement implements L2_Spec<JaristaGoal> {
    public static final StateManagement INSTANCE = new StateManagement();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE);
    }
}
