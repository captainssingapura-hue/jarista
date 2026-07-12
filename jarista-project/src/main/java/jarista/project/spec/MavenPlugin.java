package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Maven plugin that compiles .jarista/, walks the graph, outputs a structured artifact. */
public class MavenPlugin implements L3_Spec<BuildIntegration>, LoadBearing {
    public static final MavenPlugin INSTANCE = new MavenPlugin();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
