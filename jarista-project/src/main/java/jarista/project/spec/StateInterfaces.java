package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — StateStore, PieceStatus, Freshness — the contracts for externalized state. */
public class StateInterfaces implements L3_Spec<CoreTypeSystem> {
    public static final StateInterfaces INSTANCE = new StateInterfaces();
}
