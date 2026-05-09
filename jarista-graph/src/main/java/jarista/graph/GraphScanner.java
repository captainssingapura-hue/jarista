package jarista.graph;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import jarista.Stateless;

import java.util.ArrayList;
import java.util.List;

public final class GraphScanner {

    private final String[] packages;

    public GraphScanner(String... packages) {
        this.packages = packages;
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends Stateless>> scan() {
        try (ScanResult result = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(packages)
                .scan()) {

            var classes = new ArrayList<Class<? extends Stateless>>();
            for (ClassInfo ci : result.getClassesImplementing(Stateless.class.getName())) {
                Class<?> cls = ci.loadClass();
                if (!cls.isInterface()) {
                    classes.add((Class<? extends Stateless>) cls);
                }
            }
            return List.copyOf(classes);
        }
    }
}
