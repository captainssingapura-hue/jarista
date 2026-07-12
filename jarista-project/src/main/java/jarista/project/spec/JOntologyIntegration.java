package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Jarista Stateless extends jOntology Stateless. Type hierarchy unification. */
public class JOntologyIntegration implements L3_Spec<CoreTypeSystem>, LoadBearing {
    public static final JOntologyIntegration INSTANCE = new JOntologyIntegration();
    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
