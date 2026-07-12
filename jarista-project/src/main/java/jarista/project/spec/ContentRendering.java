package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Multi-target rendering pipeline for node content.
 *
 * <p>The same Detail/Resource data rendered differently per target:
 * FX desktop panel, HTML report export, TUI fallback, Markdown dump.
 * Pattern matching on Detail variants drives each renderer.
 */
public class ContentRendering implements L3_Spec<ContentManagement>, LoadBearing {
    public static final ContentRendering INSTANCE = new ContentRendering();

    @Override public SpecStatus status() { return SpecStatus.IN_PROGRESS; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("FX renderer (current): Text → Label, Code → styled TextArea,")
                .ln("  Html → placeholder label, Resource → placeholder.")
                .ln("HTML export (planned): full rendering with embedded images,")
                .ln("  MathJax for equations, syntax-highlighted code blocks.")
                .ln("TUI fallback: Text → stdout, Code → indented, others → path ref.")
            .as(DESIGN),

            _db.text()
                .ln("switch (detail) {")
                .ln("    case Text t    -> renderText(t);")
                .ln("    case Code c    -> renderCode(c);")
                .ln("    case Html h    -> renderHtml(h);")
                .ln("    case Resource r -> renderResource(r);")
                .ln("}")
            .as(EXAMPLE),

            _db.text()
                .ln("Pattern matching ensures exhaustiveness — adding a new Detail")
                .ln("variant forces every renderer to handle it. No silent fallthrough.")
            .as(RATIONALE)
        );
    }
}
