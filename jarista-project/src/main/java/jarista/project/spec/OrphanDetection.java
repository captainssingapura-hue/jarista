package jarista.project.spec;

import jarista.Stateless;
import jarista.spec.L3_Spec;

import java.util.Set;

/** L3 — Specs with no @ForFeature link = unimplemented. Flag them. */
public class OrphanDetection implements L3_Spec<GraphValidation> {
    public static final OrphanDetection INSTANCE = new OrphanDetection();

    public Set<Stateless> dependencies() {
        return Set.of(ClasspathScanning.INSTANCE);
    }
}
