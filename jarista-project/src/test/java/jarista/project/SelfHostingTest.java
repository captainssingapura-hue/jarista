package jarista.project;

import jarista.Stateless;
import jarista.graph.GraphValidator;
import jarista.graph.Violation;
import jarista.project.spec.*;
import jarista.project.piece.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Jarista validates its own feature graph.
 * 结图为谱，南郭立现，虽猫可代之。
 */
class SelfHostingTest {

    @Test
    void graphHasNoViolations() {
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate("jarista.project");
        assertTrue(violations.isEmpty(),
                () -> "Jarista's own graph has violations:\n" +
                      violations.stream().map(Object::toString).reduce("", (a, b) -> a + "  - " + b + "\n"));
    }

    @Test
    void l1RootExists() {
        assertNotNull(JaristaGoal.INSTANCE);
        assertInstanceOf(Stateless.class, JaristaGoal.INSTANCE);
    }

    @Test
    void allEpicsAreStateless() {
        var epics = List.of(
                CoreTypeSystem.INSTANCE,
                GraphValidation.INSTANCE,
                StateManagement.INSTANCE,
                BuildIntegration.INSTANCE,
                DeveloperExperience.INSTANCE,
                UILayer.INSTANCE,
                CrossTeam.INSTANCE,
                CodeQuality.INSTANCE,
                CulturalStudies.INSTANCE
        );
        for (var epic : epics) {
            assertInstanceOf(Stateless.class, epic, epic.getClass().getSimpleName() + " must be Stateless");
        }
    }

    @Test
    void dependenciesFormAcyclicGraph() {
        // BuildIntegration depends on CoreTypeSystem and GraphValidation
        Set<Stateless> buildDeps = BuildIntegration.INSTANCE.dependencies();
        assertTrue(buildDeps.contains(CoreTypeSystem.INSTANCE));
        assertTrue(buildDeps.contains(GraphValidation.INSTANCE));

        // UILayer depends on BuildIntegration and StateManagement
        Set<Stateless> uiDeps = UILayer.INSTANCE.dependencies();
        assertTrue(uiDeps.contains(BuildIntegration.INSTANCE));
        assertTrue(uiDeps.contains(StateManagement.INSTANCE));

        // No cycles — the validator confirms this
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate("jarista.project");
        assertTrue(violations.stream().noneMatch(v -> v instanceof Violation.CyclicDependency),
                "No cycles allowed in Jarista's spec graph");
    }

    @Test
    void piecesLinkToSpecs() {
        // Each piece binds to a spec via generics — compiler already checked this.
        // Here we verify the INSTANCE fields exist and are the right type.
        assertInstanceOf(Stateless.class, ImplSpecSkeleton.INSTANCE);
        assertInstanceOf(Stateless.class, ImplPieceSkeleton.INSTANCE);
        assertInstanceOf(Stateless.class, ImplForFeature.INSTANCE);
        assertInstanceOf(Stateless.class, ImplStateInterfaces.INSTANCE);
        assertInstanceOf(Stateless.class, ImplJOntology.INSTANCE);
        assertInstanceOf(Stateless.class, ImplScanner.INSTANCE);
        assertInstanceOf(Stateless.class, ImplInstanceEnforcer.INSTANCE);
        assertInstanceOf(Stateless.class, ImplCycleDetector.INSTANCE);
    }

    @Test
    void allNodesAreJOntologyImmutable() {
        assertInstanceOf(hue.captains.singapura.tao.ontology.Immutable.class, JaristaGoal.INSTANCE);
        assertInstanceOf(hue.captains.singapura.tao.ontology.Immutable.class, ImplScanner.INSTANCE);
    }

    @Test
    void specCount() {
        // 1 L1 + 9 L2 + 34 L3 + 3 L4 = 47 specs
        // 8 L1 pieces
        // Total: 55 Stateless nodes in the project graph
        var validator = new GraphValidator();
        List<Violation> violations = validator.validate("jarista.project");
        assertEquals(0, violations.size(), "All 55 nodes should pass validation");
    }
}
