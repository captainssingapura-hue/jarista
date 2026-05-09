package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Git-friendly, file-per-piece state store. The reference implementation. */
public class FileBasedStateStore implements L3_Spec<StateManagement> {
    public static final FileBasedStateStore INSTANCE = new FileBasedStateStore();
}
