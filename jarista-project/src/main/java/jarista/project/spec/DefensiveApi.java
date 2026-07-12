package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Tighten APIs: unmodifiable collections, proper scoping, modern Java idioms. */
public class DefensiveApi implements L3_Spec<CodeQuality>, LoadBearing {
    public static final DefensiveApi INSTANCE = new DefensiveApi();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
