package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — ClassGraph-based discovery of all Stateless implementations on the classpath. */
public class ClasspathScanning implements L3_Spec<GraphValidation>, LoadBearing {
    public static final ClasspathScanning INSTANCE = new ClasspathScanning();
    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
