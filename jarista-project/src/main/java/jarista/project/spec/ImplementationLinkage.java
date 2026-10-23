package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — ImplementationFor: a non-intrusive link from an implementation class to
 * its spec, declared in a Linkage that depends on both. The alternative to
 * {@code @ForFeature}.
 */
public class ImplementationLinkage implements L3_Spec<CoreTypeSystem>, LoadBearing {
    public static final ImplementationLinkage INSTANCE = new ImplementationLinkage();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("A typed record, ImplementationFor<C, S extends Spec>(Class<C> implementation, S spec),")
                .ln("pairs an implementation class with the spec it fulfils.")
                .ln("Links are declared in a Linkage — the one place that depends on both sides.")
            .as(DESCRIPTION),

            _db.text()
                .ln("@ForFeature is intrusive: the annotation sits on the implementation,")
                .ln("so implementation code must import and name its spec.")
                .ln("ImplementationFor inverts that. Neither the implementation nor the spec")
                .ln("references the other; only the linkage module knows both.")
            .as(RATIONALE),

            _db.code("java")
                .ln("public final class JaristaLinkage implements Linkage {")
                .ln("    public static final JaristaLinkage INSTANCE = new JaristaLinkage();")
                .ln("    @Override public List<ImplementationFor<?, ?>> links() {")
                .ln("        return List.of(")
                .ln("            ImplementationFor.of(ForFeature.class, FeatureAnnotation.INSTANCE),")
                .ln("            ImplementationFor.of(StateStore.class, StateInterfaces.INSTANCE));")
                .ln("    }")
                .ln("}")
            .as(EXAMPLE),

            _db.text()
                .ln("S is bounded to Spec, the marker every L0–L18 level extends, so linking")
                .ln("an implementation to a piece — or to any non-spec node — is a compile error.")
                .ln("Linkage is deliberately not Stateless: it is not a graph node and must not")
                .ln("be picked up by the graph scanners. LinkageScanner discovers it separately.")
            .as(DESIGN)
        );
    }
}
