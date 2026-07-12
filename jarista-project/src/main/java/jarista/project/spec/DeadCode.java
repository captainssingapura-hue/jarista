package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Remove unused constants, dead classes, and unreachable code. */
public class DeadCode implements L3_Spec<CodeQuality>, LoadBearing {
    public static final DeadCode INSTANCE = new DeadCode();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
