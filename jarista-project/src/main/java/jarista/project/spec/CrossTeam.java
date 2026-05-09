package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Proxy nodes, remote graph sync, cross-team collaboration.
 * A typed reference to someone else's graph. No shared server. No shared process.
 */
public class CrossTeam implements L2_Spec<JaristaGoal> {
    public static final CrossTeam INSTANCE = new CrossTeam();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE, BuildIntegration.INSTANCE);
    }
}
