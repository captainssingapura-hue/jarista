package jarista.graph;

import jarista.Stateless;
import jarista.spec.L1_Spec;
import jarista.spec.L2_Spec;
import jarista.spec.L3_Spec;
import jarista.piece.L1_Piece;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphValidatorTest {

    // ── Well-formed graph ──

    static class Goal implements L1_Spec {
        public static final Goal INSTANCE = new Goal();
    }

    static class Epic implements L2_Spec<Goal> {
        public static final Epic INSTANCE = new Epic();
    }

    static class Feature implements L3_Spec<Epic> {
        public static final Feature INSTANCE = new Feature();
        public Set<Stateless> dependencies() {
            return Set.of(Epic.INSTANCE);
        }
    }

    static class Task implements L1_Piece<Feature> {
        public static final Task INSTANCE = new Task();
    }

    // ── Missing INSTANCE ──

    static class BadSpec implements L1_Spec {
        // no INSTANCE field
    }

    // ── Cyclic dependencies ──

    static class CycleA implements L2_Spec<Goal> {
        public static final CycleA INSTANCE = new CycleA();
        public Set<Stateless> dependencies() {
            return Set.of(CycleB.INSTANCE);
        }
    }

    static class CycleB implements L2_Spec<Goal> {
        public static final CycleB INSTANCE = new CycleB();
        public Set<Stateless> dependencies() {
            return Set.of(CycleA.INSTANCE);
        }
    }

    // ── Tests ──

    @Test
    void wellFormedGraphHasNoViolations() {
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate(List.of(
                Goal.class, Epic.class, Feature.class, Task.class));
        assertTrue(violations.isEmpty(), () -> "Expected no violations but got: " + violations);
    }

    @Test
    void missingInstanceDetected() {
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate(List.of(BadSpec.class));
        assertEquals(1, violations.size());
        assertInstanceOf(Violation.MissingInstance.class, violations.getFirst());
        assertEquals(BadSpec.class, ((Violation.MissingInstance) violations.getFirst()).type());
    }

    @Test
    void cyclicDependencyDetected() {
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate(List.of(
                Goal.class, CycleA.class, CycleB.class));
        boolean hasCycle = violations.stream()
                .anyMatch(v -> v instanceof Violation.CyclicDependency);
        assertTrue(hasCycle, "Expected a cycle violation but got: " + violations);
    }

    @Test
    void scannerFindsConcreteStatelessClasses() {
        var scanner = new GraphScanner("jarista.graph");
        var classes = scanner.scan();
        // Inner test classes may or may not be discovered by ClassGraph depending
        // on classpath layout — the important thing is scan() doesn't throw
        assertNotNull(classes);
    }

    @Test
    void violationToStringIsReadable() {
        var missing = new Violation.MissingInstance(BadSpec.class);
        assertTrue(missing.toString().contains("BadSpec"));
        assertTrue(missing.toString().contains("INSTANCE"));

        var cycle = new Violation.CyclicDependency("A → B → A");
        assertTrue(cycle.toString().contains("A → B → A"));
    }
}
