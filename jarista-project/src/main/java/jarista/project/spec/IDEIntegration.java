package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — IntelliJ/Eclipse plugin for quick-create pieces, graph navigation, live preview. */
public class IDEIntegration implements L3_Spec<DeveloperExperience>, LoadBearing {
    public static final IDEIntegration INSTANCE = new IDEIntegration();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
