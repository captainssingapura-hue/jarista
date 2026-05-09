package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Extract duplicated reflection logic (specLevel, pieceLevel, extractParent, readDependencies) into jarista-graph. */
public class SharedReflection implements L3_Spec<CodeQuality> {
    public static final SharedReflection INSTANCE = new SharedReflection();
}
