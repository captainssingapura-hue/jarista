package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — StateStore, PieceStatus, Freshness — the contracts for externalized state. */
public class StateInterfaces implements L3_Spec<CoreTypeSystem>, LoadBearing {
    public static final StateInterfaces INSTANCE = new StateInterfaces();
    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
