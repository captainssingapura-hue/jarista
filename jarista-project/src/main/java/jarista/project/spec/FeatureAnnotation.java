package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — @ForFeature annotation linking implementation code to specs. */
public class FeatureAnnotation implements L3_Spec<CoreTypeSystem>, LoadBearing {
    public static final FeatureAnnotation INSTANCE = new FeatureAnnotation();
    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
