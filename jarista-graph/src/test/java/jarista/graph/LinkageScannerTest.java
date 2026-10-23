package jarista.graph;

import jarista.link.ImplementationFor;
import jarista.link.Linkage;
import jarista.spec.L0_Spec;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkageScannerTest {

    static class Vision implements L0_Spec {
        public static final Vision INSTANCE = new Vision();
    }

    static class Service {}

    static class SampleLinkage implements Linkage {
        public static final SampleLinkage INSTANCE = new SampleLinkage();
        @Override public List<ImplementationFor<?, ?>> links() {
            return List.of(ImplementationFor.of(Service.class, Vision.INSTANCE));
        }
    }

    static class NoInstanceLinkage implements Linkage {
        // no INSTANCE field — must be skipped, not crash the scan
        @Override public List<ImplementationFor<?, ?>> links() { return List.of(); }
    }

    @Test
    void collectsLinksFromGivenLinkages() {
        var links = LinkageScanner.collect(List.of(SampleLinkage.class, NoInstanceLinkage.class));
        assertEquals(1, links.size());
        assertEquals(Service.class, links.getFirst().implementation());
        assertSame(Vision.INSTANCE, links.getFirst().spec());
    }

    @Test
    void classpathScanDoesNotThrow() {
        // Inner test classes may or may not be discovered by ClassGraph depending on
        // classpath layout (as for GraphScanner) — the contract here is that scanning
        // a package completes and returns a list.
        var scanner = new LinkageScanner("jarista.graph");
        assertNotNull(scanner.scan());
        assertNotNull(scanner.links());
    }
}
