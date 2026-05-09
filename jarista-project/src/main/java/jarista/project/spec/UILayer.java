package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L2_Spec;

import java.util.Set;

/**
 * L2 — Visualization, stakeholder view, developer view.
 * The graph rendered for humans who don't want to read Java.
 */
public class UILayer implements L2_Spec<JaristaGoal> {
    public static final UILayer INSTANCE = new UILayer();

    public Set<Stateless> dependencies() {
        return Set.of(BuildIntegration.INSTANCE, StateManagement.INSTANCE);
    }
}
