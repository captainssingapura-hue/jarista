package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Centralize dependency and plugin versions in parent POM via dependencyManagement. */
public class PomHygiene implements L3_Spec<CodeQuality>, LoadBearing {
    public static final PomHygiene INSTANCE = new PomHygiene();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
