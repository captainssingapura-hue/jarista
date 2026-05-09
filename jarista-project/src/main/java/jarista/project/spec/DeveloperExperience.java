package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — CLI tools, IDE integration, piece generators.
 * Reduce the friction of creating specs and pieces to near-zero.
 */
public class DeveloperExperience implements L2_Spec<JaristaGoal> {
    public static final DeveloperExperience INSTANCE = new DeveloperExperience();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE);
    }
}
