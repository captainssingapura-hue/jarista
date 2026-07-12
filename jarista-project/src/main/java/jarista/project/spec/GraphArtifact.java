package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Structured graph output from build: JSON/binary representation for UI consumption. */
public class GraphArtifact implements L3_Spec<BuildIntegration>, LoadBearing {
    public static final GraphArtifact INSTANCE = new GraphArtifact();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
