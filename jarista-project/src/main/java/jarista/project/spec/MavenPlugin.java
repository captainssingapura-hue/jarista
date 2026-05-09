package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Maven plugin that compiles .jarista/, walks the graph, outputs a structured artifact. */
public class MavenPlugin implements L3_Spec<BuildIntegration> {
    public static final MavenPlugin INSTANCE = new MavenPlugin();
}
