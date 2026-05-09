package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Fix stale node counts and outdated comments across the codebase. */
public class StaleComments implements L3_Spec<CodeQuality> {
    public static final StaleComments INSTANCE = new StaleComments();
}
