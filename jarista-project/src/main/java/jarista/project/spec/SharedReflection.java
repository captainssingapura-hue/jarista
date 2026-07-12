package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Extract duplicated reflection logic (specLevel, pieceLevel, extractParent, readDependencies) into jarista-graph. */
public class SharedReflection implements L3_Spec<CodeQuality>, LoadBearing {
    public static final SharedReflection INSTANCE = new SharedReflection();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
