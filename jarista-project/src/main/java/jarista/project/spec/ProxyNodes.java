package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Typed reference to a spec in another team's graph. The cross-team handshake. */
public class ProxyNodes implements L3_Spec<CrossTeam>, LoadBearing {
    public static final ProxyNodes INSTANCE = new ProxyNodes();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
