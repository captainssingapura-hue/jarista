package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Markdown vs HTML is a false dichotomy.
 *
 * <p>Both are presentation-layer constructs. Neither knows whether
 * the paragraph it carries is a description, a rationale, or an example.
 * The real question is: does your content system know what it's carrying?
 */
public class RepresentationLayerFallacy implements L3_Spec<ContentManagement>, LoadBearing {
    public static final RepresentationLayerFallacy INSTANCE = new RepresentationLayerFallacy();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Three layers that the md-vs-html debate collapses into one:")
                .ln("")
                .ln("  1. WHAT — the content type (prose, code, diagram, equation)")
                .ln("  2. WHY  — the semantic role (description, rationale, design, example)")
                .ln("  3. HOW  — the rendering target (markdown, HTML, FX panel, PDF)")
                .ln("")
                .ln("Markdown answers layer 3. HTML answers layer 3.")
                .ln("Neither answers layers 1 or 2.")
                .ln("")
                .ln("Jarista's Detail ADT captures layer 1 (sealed variants).")
                .ln("DetailRole captures layer 2 (enum).")
                .ln("The renderer — whatever format it emits — is layer 3.")
                .ln("")
                .ln("Arguing md vs html is arguing about which paint to use")
                .ln("before deciding what the painting is of.")
            .as(DESIGN),

            _db.text()
                .ln("Markdown: a paragraph is a paragraph.")
                .ln("HTML: a <p> is a <p>.")
                .ln("Neither knows if that paragraph is a DESCRIPTION or a RATIONALE.")
                .ln("")
                .ln("You can impose convention — <div class='rationale'> —")
                .ln("but that's inventing a type system in strings.")
                .ln("Convention is the compile-time checking of people who gave up")
                .ln("on compile-time checking.")
                .ln("")
                .ln("The Detail ADT makes content identity compiled and validated.")
                .ln("The rendering format becomes a projection — derived, not curated.")
                .ln("Same pattern as organizational status: the observer's view")
                .ln("is a function of the worker's reality.")
            .as(RATIONALE)
        );
    }
}
