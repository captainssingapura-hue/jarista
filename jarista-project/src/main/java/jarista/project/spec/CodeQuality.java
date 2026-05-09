package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Internal code quality, consistency, and polish.
 * Keep the house clean so the structure stays honest.
 */
public class CodeQuality implements L2_Spec<JaristaGoal> {
    public static final CodeQuality INSTANCE = new CodeQuality();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE, GraphValidation.INSTANCE);
    }
}
