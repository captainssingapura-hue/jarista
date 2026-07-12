package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — CLI for piece generation, graph querying, status updates. */
public class CLITools implements L3_Spec<DeveloperExperience>, LoadBearing {
    public static final CLITools INSTANCE = new CLITools();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
