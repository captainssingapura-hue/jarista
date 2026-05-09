package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Centralize dependency and plugin versions in parent POM via dependencyManagement. */
public class PomHygiene implements L3_Spec<CodeQuality> {
    public static final PomHygiene INSTANCE = new PomHygiene();
}
