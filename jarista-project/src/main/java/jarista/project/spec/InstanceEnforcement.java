package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;

import java.util.Set;

/** L3 — Every Stateless class must have public static final INSTANCE. */
public class InstanceEnforcement implements L3_Spec<GraphValidation> {
    public static final InstanceEnforcement INSTANCE = new InstanceEnforcement();

    public Set<Stateless> dependencies() {
        return Set.of(ClasspathScanning.INSTANCE);
    }
}
