package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.Set;

/** L3 — Every Stateless class must have public static final INSTANCE. */
public class InstanceEnforcement implements L3_Spec<GraphValidation>, LoadBearing {
    public static final InstanceEnforcement INSTANCE = new InstanceEnforcement();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    public Set<Stateless> dependencies() {
        return Set.of(ClasspathScanning.INSTANCE);
    }
}
