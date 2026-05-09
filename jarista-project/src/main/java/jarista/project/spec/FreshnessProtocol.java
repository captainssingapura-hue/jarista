package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;

import java.util.Set;

/** L3 — Staleness detection, acknowledgment protocol, freshness cascading. */
public class FreshnessProtocol implements L3_Spec<StateManagement> {
    public static final FreshnessProtocol INSTANCE = new FreshnessProtocol();

    public Set<Stateless> dependencies() {
        return Set.of(FileBasedStateStore.INSTANCE);
    }
}
