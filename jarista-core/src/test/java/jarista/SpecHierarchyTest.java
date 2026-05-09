package jarista;

import jarista.annotation.ForFeature;
import jarista.piece.L1_Piece;
import jarista.piece.L2_Piece;
import jarista.spec.L1_Spec;
import jarista.spec.L2_Spec;
import jarista.spec.L3_Spec;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpecHierarchyTest {

    // ── Sample spec graph ──

    static class ProjectGoal implements L1_Spec {
        public static final ProjectGoal INSTANCE = new ProjectGoal();
    }

    static class AuthEpic implements L2_Spec<ProjectGoal> {
        public static final AuthEpic INSTANCE = new AuthEpic();
    }

    static class OAuthFlow implements L3_Spec<AuthEpic> {
        public static final OAuthFlow INSTANCE = new OAuthFlow();
        Set<Stateless> dependencies() {
            return Set.of(AuthEpic.INSTANCE);
        }
    }

    // ── Sample pieces ──

    static class ImplOAuth implements L1_Piece<OAuthFlow> {
        public static final ImplOAuth INSTANCE = new ImplOAuth();
    }

    static class WriteCallback implements L2_Piece<ImplOAuth> {
        public static final WriteCallback INSTANCE = new WriteCallback();
    }

    // ── Sample annotated implementation ──

    @ForFeature(OAuthFlow.class)
    static class TokenRefreshService {}

    // ── Tests ──

    @Test
    void specInstancesAreStateless() {
        assertInstanceOf(Stateless.class, ProjectGoal.INSTANCE);
        assertInstanceOf(Stateless.class, AuthEpic.INSTANCE);
        assertInstanceOf(Stateless.class, OAuthFlow.INSTANCE);
    }

    @Test
    void specInstancesAreJOntologyImmutable() {
        assertInstanceOf(hue.captains.singapura.tao.ontology.Immutable.class, ProjectGoal.INSTANCE);
        assertInstanceOf(hue.captains.singapura.tao.ontology.Stateless.class, OAuthFlow.INSTANCE);
    }

    @Test
    void pieceInstancesAreStateless() {
        assertInstanceOf(Stateless.class, ImplOAuth.INSTANCE);
        assertInstanceOf(Stateless.class, WriteCallback.INSTANCE);
    }

    @Test
    void dependenciesReturnSpecInstances() {
        Set<Stateless> deps = OAuthFlow.INSTANCE.dependencies();
        assertEquals(1, deps.size());
        assertTrue(deps.contains(AuthEpic.INSTANCE));
    }

    @Test
    void forFeatureAnnotationIsPresent() {
        ForFeature annotation = TokenRefreshService.class.getAnnotation(ForFeature.class);
        assertNotNull(annotation);
        assertEquals(OAuthFlow.class, annotation.value());
    }

    @Test
    void graphTraversalViaDependencies() {
        var spec = OAuthFlow.INSTANCE;
        var deps = spec.dependencies();

        for (Stateless dep : deps) {
            assertNotNull(dep);
            assertInstanceOf(L2_Spec.class, dep);
        }
    }
}
