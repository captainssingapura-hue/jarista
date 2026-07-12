package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Mathematical notation support for theoretical content.
 *
 * <p>Heuristics and algorithms need equations to be theoretically sound.
 * LaTeX strings in a typed wrapper, rendered to MathML or SVG
 * depending on the target.
 */
public class MathNotation implements L3_Spec<ContentManagement>, LoadBearing {
    public static final MathNotation INSTANCE = new MathNotation();

    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("New Detail variant: Math(String latex)")
                .ln("Or a ResourceType.LATEX for external .tex files.")
                .ln("FX viewer renders via MathML or pre-rendered SVG.")
                .ln("HTML export uses MathJax or KaTeX.")
            .as(DESIGN),

            _db.text()
                .ln("In the notation project, drum density analysis uses probability")
                .ln("distributions, tempo models involve interpolation curves, and")
                .ln("voice separation relies on graph-theoretic cost functions.")
                .ln("Without inline math, these specs are incomplete — the reader")
                .ln("must chase down a separate document to understand the algorithm.")
            .as(RATIONALE),

            _db.text()
                .ln("_db.math().ln('P(hit) = \\\\frac{density}{max\\\\_density}').as(DESIGN)")
                .ln("_db.resource('tempo-model.tex', LATEX).as(REFERENCE)")
            .as(EXAMPLE)
        );
    }
}
