package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Maven plugin, graph artifact generation, .jarista/ source root support.
 * Makes the graph a first-class build citizen.
 */
public class BuildIntegration implements L2_Spec<JaristaGoal> {
    public static final BuildIntegration INSTANCE = new BuildIntegration();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE, GraphValidation.INSTANCE);
    }
}
