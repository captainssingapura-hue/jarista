package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.Set;

/** L3 — Staleness detection, acknowledgment protocol, freshness cascading. */
public class FreshnessProtocol implements L3_Spec<StateManagement>, LoadBearing {
    public static final FreshnessProtocol INSTANCE = new FreshnessProtocol();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    public Set<Stateless> dependencies() {
        return Set.of(FileBasedStateStore.INSTANCE);
    }
}
