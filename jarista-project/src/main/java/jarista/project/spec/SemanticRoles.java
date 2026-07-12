package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — DetailRole enum and RoledDetail product type.
 *
 * <p>Eight semantic roles orthogonal to content type:
 * Description, Expectation, Rationale, Design, Example, Reference, Product, Note.
 */
public class SemanticRoles implements L3_Spec<ContentManagement>, LoadBearing {
    public static final SemanticRoles INSTANCE = new SemanticRoles();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("RoledDetail(role, detail) — product type combining both axes.")
                .ln("A Text can be a DESCRIPTION or an EXAMPLE.")
                .ln("An SVG Resource can be a DESIGN diagram or a PRODUCT deliverable.")
                .ln("Role and content type never constrain each other.")
            .as(DESIGN),

            _db.text()
                .ln("DESCRIPTION — what this node is about")
                .ln("EXPECTATION — acceptance criteria or success conditions")
                .ln("RATIONALE   — why this approach, what alternatives were rejected")
                .ln("DESIGN      — architecture, data model, type structure")
                .ln("EXAMPLE     — concrete usage or sample")
                .ln("REFERENCE   — links to external material")
                .ln("PRODUCT     — deliverable artifact (SVG cartoon, generated report)")
                .ln("NOTE        — transient commentary, caveats")
            .as(REFERENCE)
        );
    }
}
