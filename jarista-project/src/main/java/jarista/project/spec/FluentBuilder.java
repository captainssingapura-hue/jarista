package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — DetailBuilder DSL for ergonomic detail construction.
 *
 * <p>Each entry: {@code _db.text().ln(...).as(ROLE)} → single {@code RoledDetail}.
 * No mutable accumulator, no build() — each call produces an immutable value
 * for use in {@code List.of(...)}.
 */
public class FluentBuilder implements L3_Spec<ContentManagement>, LoadBearing {
    public static final FluentBuilder INSTANCE = new FluentBuilder();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("_db.text().ln('line 1').ln('line 2').as(DESCRIPTION)")
                .ln("_db.code('java').ln('sealed interface Detail {}').as(DESIGN)")
                .ln("_db.html().ln('<svg>...</svg>').as(PRODUCT)")
                .ln("_db.resource('diagram.svg', SVG).as(DESIGN)")
            .as(EXAMPLE),

            _db.text()
                .ln("Inner builders (TextBlock, CodeBlock, HtmlBlock, ResourceRef)")
                .ln("each terminate with .as(role) returning RoledDetail directly.")
                .ln("The DetailBuilder itself is stateless — reusable across entries.")
            .as(DESIGN)
        );
    }
}
