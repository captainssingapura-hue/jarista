package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Sealed Detail interface: Text, Code, Html, Resource.
 *
 * <p>Four variants, purely about <em>what</em> the content is.
 * Orthogonal to role (why it exists) — combined via {@code RoledDetail}.
 */
public class TypedDetailADT implements L3_Spec<ContentManagement>, LoadBearing {
    public static final TypedDetailADT INSTANCE = new TypedDetailADT();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("sealed interface Detail {")
                .ln("    record Text(String content)")
                .ln("    record Code(String language, String content)")
                .ln("    record Html(String content)")
                .ln("    record Resource(String path, ResourceType type)")
                .ln("}")
            .as(DESIGN),

            _db.text()
                .ln("Exhaustive pattern matching — every renderer must handle all four.")
                .ln("No null content, no stringly-typed 'type' field.")
            .as(RATIONALE)
        );
    }
}
