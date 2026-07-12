package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Expand/collapse all, keyboard navigation, breadcrumb trail. */
public class TreeControls implements L3_Spec<UILayer>, LoadBearing {
    public static final TreeControls INSTANCE = new TreeControls();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
