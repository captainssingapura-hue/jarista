package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;

import java.util.Set;

/** L3 — Cache, sync, and staleness handling for proxy nodes referencing remote graphs. */
public class RemoteGraphSync implements L3_Spec<CrossTeam> {
    public static final RemoteGraphSync INSTANCE = new RemoteGraphSync();

    public Set<Stateless> dependencies() {
        return Set.of(ProxyNodes.INSTANCE);
    }
}
