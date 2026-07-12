package jarista.graph;

import jarista.Stateless;

import java.util.ArrayList;
import java.util.List;

public final class GraphValidator {

    private final InstanceEnforcer instanceEnforcer = new InstanceEnforcer();
    private final CycleDetector cycleDetector = new CycleDetector();
    private final LoadBearingEnforcer loadBearingEnforcer = new LoadBearingEnforcer();

    public List<Violation> validate(String... packages) {
        List<Class<? extends Stateless>> classes = new GraphScanner(packages).scan();
        return validate(classes);
    }

    public List<Violation> validate(List<Class<? extends Stateless>> classes) {
        var violations = new ArrayList<Violation>();
        violations.addAll(instanceEnforcer.check(classes));
        violations.addAll(cycleDetector.check(classes));
        violations.addAll(loadBearingEnforcer.check(classes));
        return List.copyOf(violations);
    }
}
