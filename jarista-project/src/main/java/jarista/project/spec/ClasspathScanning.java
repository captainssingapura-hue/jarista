package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — ClassGraph-based discovery of all Stateless implementations on the classpath. */
public class ClasspathScanning implements L3_Spec<GraphValidation> {
    public static final ClasspathScanning INSTANCE = new ClasspathScanning();
}
