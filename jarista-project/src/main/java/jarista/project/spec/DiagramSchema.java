package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Conventions for SVG/PNG diagrams attached to nodes.
 *
 * <p>Diagrams are the lingua franca of technical explanation.
 * Typed resource references with sizing, alt-text, and
 * co-location conventions so diagrams travel with their specs.
 */
public class DiagramSchema implements L3_Spec<ContentManagement>, LoadBearing {
    public static final DiagramSchema INSTANCE = new DiagramSchema();

    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Resource directory convention:")
                .ln("  src/main/resources/docs/<spec-name>/")
                .ln("    overview.md       — prose explanation")
                .ln("    architecture.svg  — structural diagram")
                .ln("    flow.png          — process diagram")
                .ln("    equations.tex     — mathematical content")
            .as(DESIGN),

            _db.text()
                .ln("SVG preferred for diagrams — scalable, versionable, diffable.")
                .ln("PNG for screenshots or generated visuals.")
                .ln("Co-location ensures diagrams move with the module, not abandoned")
                .ln("in a wiki that rots when the code evolves.")
            .as(RATIONALE)
        );
    }
}
