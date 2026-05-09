package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Remove unused constants, dead classes, and unreachable code. */
public class DeadCode implements L3_Spec<CodeQuality> {
    public static final DeadCode INSTANCE = new DeadCode();
}
