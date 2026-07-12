package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Graph-level validation of content integrity.
 *
 * <p>Every {@code Detail.Resource} path must resolve at build time.
 * Broken links are violations, caught by the graph validator —
 * not discovered at render time when the user clicks a node.
 */
public class ContentValidation implements L3_Spec<ContentManagement>, LoadBearing {
    public static final ContentValidation INSTANCE = new ContentValidation();

    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("New Violation variant: BrokenResource(spec, path, type).")
                .ln("Scans all Stateless nodes, collects Detail.Resource entries,")
                .ln("checks classpath resolution for each.")
            .as(DESIGN),

            _db.text()
                .ln("Catch broken links at compile time, not at render time.")
                .ln("Resources travel with the graph — if the spec compiles, its content exists.")
            .as(RATIONALE)
        );
    }
}
