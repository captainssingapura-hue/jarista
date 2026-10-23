package jarista.link;

import jarista.piece.L1_Piece;
import jarista.spec.L0_Spec;
import jarista.spec.L1_Spec;
import jarista.spec.L3_Spec;
import jarista.spec.Spec;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImplementationForTest {

    // ── Sample specs (no reference to any implementation) ──

    static class Vision implements L0_Spec {
        public static final Vision INSTANCE = new Vision();
    }

    static class Goal implements L1_Spec<Vision> {
        public static final Goal INSTANCE = new Goal();
    }

    // ── Sample implementation (no reference to any spec) ──

    static class TokenRefreshService {}

    // ── The linkage: the only place that knows both ──

    static class SampleLinkage implements Linkage {
        public static final SampleLinkage INSTANCE = new SampleLinkage();
        @Override public List<ImplementationFor<?, ?>> links() {
            return List.of(ImplementationFor.of(TokenRefreshService.class, Goal.INSTANCE));
        }
    }

    @Test
    void linkPairsImplementationClassWithSpecInstance() {
        var link = ImplementationFor.of(TokenRefreshService.class, Goal.INSTANCE);
        assertEquals(TokenRefreshService.class, link.implementation());
        assertSame(Goal.INSTANCE, link.spec());
    }

    @Test
    void linkageDeclaresLinksWithoutHardLinks() {
        List<ImplementationFor<?, ?>> links = SampleLinkage.INSTANCE.links();
        assertEquals(1, links.size());
        assertEquals(TokenRefreshService.class, links.getFirst().implementation());
        assertSame(Goal.INSTANCE, links.getFirst().spec());
    }

    @Test
    void everySpecLevelIsASpecAndPiecesAreNot() {
        // The S extends Spec bound admits any level …
        assertTrue(Spec.class.isAssignableFrom(L0_Spec.class));
        assertTrue(Spec.class.isAssignableFrom(L1_Spec.class));
        assertTrue(Spec.class.isAssignableFrom(L3_Spec.class));
        // … and rejects pieces: ImplementationFor.of(x, somePiece) would not compile.
        assertFalse(Spec.class.isAssignableFrom(L1_Piece.class));
    }

    @Test
    void linkageIsNotAGraphNode() {
        // A Linkage must not be Stateless, or the graph scanners would treat it as a node.
        assertFalse(jarista.Stateless.class.isAssignableFrom(Linkage.class));
    }

    @Test
    void nullsAreRejected() {
        assertThrows(NullPointerException.class, () -> ImplementationFor.of(null, Goal.INSTANCE));
        assertThrows(NullPointerException.class, () -> ImplementationFor.of(TokenRefreshService.class, null));
    }
}
