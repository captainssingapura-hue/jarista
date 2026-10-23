package jarista.graph;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import jarista.link.ImplementationFor;
import jarista.link.Linkage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Discovers {@link Linkage} declarations on the classpath and collects their
 * {@link ImplementationFor} links — the counterpart of {@link GraphScanner}
 * for the non-intrusive spec ↔ implementation linkage.
 */
public final class LinkageScanner {

    private final String[] packages;

    public LinkageScanner(String... packages) {
        this.packages = packages;
    }

    /** Every concrete {@link Linkage} class in the scanned packages. */
    @SuppressWarnings("unchecked")
    public List<Class<? extends Linkage>> scan() {
        try (ScanResult result = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(packages)
                .scan()) {

            var classes = new ArrayList<Class<? extends Linkage>>();
            for (ClassInfo ci : result.getClassesImplementing(Linkage.class.getName())) {
                Class<?> cls = ci.loadClass();
                if (!cls.isInterface()) {
                    classes.add((Class<? extends Linkage>) cls);
                }
            }
            return List.copyOf(classes);
        }
    }

    /** Scan, then collect every link from every discovered linkage. */
    public List<ImplementationFor<?, ?>> links() {
        return collect(scan());
    }

    /**
     * Collect the links declared by the given linkage classes, read through
     * their {@code INSTANCE} fields. Exposed separately so callers (and tests)
     * can pass an explicit list instead of relying on a classpath scan.
     */
    public static List<ImplementationFor<?, ?>> collect(List<Class<? extends Linkage>> linkages) {
        var out = new ArrayList<ImplementationFor<?, ?>>();
        for (Class<? extends Linkage> cls : linkages) {
            Linkage instance = instanceOf(cls);
            if (instance != null) out.addAll(instance.links());
        }
        return List.copyOf(out);
    }

    private static Linkage instanceOf(Class<? extends Linkage> cls) {
        try {
            Field f = cls.getField("INSTANCE");
            return (Linkage) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
