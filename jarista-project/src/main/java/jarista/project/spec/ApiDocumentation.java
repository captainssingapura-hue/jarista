package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Add Javadoc to core public APIs (Stateless, spec/piece interfaces, graph scanners). */
public class ApiDocumentation implements L3_Spec<CodeQuality>, LoadBearing {
    public static final ApiDocumentation INSTANCE = new ApiDocumentation();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
