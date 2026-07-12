package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Dependency arrows or matrix in the detail panel, showing cross-branch links. */
public class DependencyView implements L3_Spec<UILayer>, LoadBearing {
    public static final DependencyView INSTANCE = new DependencyView();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
