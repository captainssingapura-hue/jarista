package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Git-friendly, file-per-piece state store. The reference implementation. */
public class FileBasedStateStore implements L3_Spec<StateManagement>, LoadBearing {
    public static final FileBasedStateStore INSTANCE = new FileBasedStateStore();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
