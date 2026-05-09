package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L2_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L2 — The compiled types: Stateless, Spec levels, Piece levels, @ForFeature, state interfaces.
 * The skeleton that makes the graph expressible in pure Java.
 */
public class CoreTypeSystem implements L2_Spec<JaristaGoal> {
    public static final CoreTypeSystem INSTANCE = new CoreTypeSystem();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("The foundational type hierarchy: Stateless marker interface,")
                .ln("L1–L18 Spec levels, L1–L8 Piece levels, and @ForFeature annotation.")
                .ln("Generics encode the parent relationship — the compiler enforces the tree.")
            .as(DESCRIPTION),

            _db.code("java")
                .ln("public class SearchFilter implements L3_Spec<UILayer> {")
                .ln("    public static final SearchFilter INSTANCE = new SearchFilter();")
                .ln("}")
            .as(EXAMPLE),

            _db.text()
                .ln("Specs use generic bounds to declare their parent:")
                .ln("L3_Spec<P extends L2_Spec<?>>.")
                .ln("This makes the hierarchy statically verifiable —")
                .ln("you cannot attach an L3 under an L1 without a compiler error.")
            .as(DESIGN)
        );
    }
}
