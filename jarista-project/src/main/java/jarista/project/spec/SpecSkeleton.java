package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — L1_Spec through L18_Spec with generic parent constraints. */
public class SpecSkeleton implements L3_Spec<CoreTypeSystem> {
    public static final SpecSkeleton INSTANCE = new SpecSkeleton();
}
