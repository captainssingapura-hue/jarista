package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Structured graph output from build: JSON/binary representation for UI consumption. */
public class GraphArtifact implements L3_Spec<BuildIntegration> {
    public static final GraphArtifact INSTANCE = new GraphArtifact();
}
