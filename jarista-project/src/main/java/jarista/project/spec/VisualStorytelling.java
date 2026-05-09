package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;
import static jarista.detail.ResourceType.*;

/** L3 — Cartoons, illustrations, and analogies that make the concepts stick. */
public class VisualStorytelling implements L3_Spec<CulturalStudies> {
    public static final VisualStorytelling INSTANCE = new VisualStorytelling();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Ideas that cannot be drawn cannot be shared.")
                .ln("Visual storytelling turns the 南郭 insight from an argument")
                .ln("into something people remember and repeat.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Key visuals to develop:")
                .ln("• The Nanguo musician hiding in the orchestra — exposed by solo")
                .ln("• The barista brewing a graph — beans to structure")
                .ln("• The woven score — threads crossing, gaps visible")
                .ln("• The Nanguo Report — dashboard of exposed fakers")
            .as(EXPECTATION),

            _db.resource("art/nanguo-orchestra.svg", SVG)
            .as(REFERENCE),

            _db.resource("art/barista-brewing.svg", SVG)
            .as(REFERENCE)
        );
    }
}
