package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Typed reference to a spec in another team's graph. The cross-team handshake. */
public class ProxyNodes implements L3_Spec<CrossTeam> {
    public static final ProxyNodes INSTANCE = new ProxyNodes();
}
