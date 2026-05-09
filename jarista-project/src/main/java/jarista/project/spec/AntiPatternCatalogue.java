package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/** L3 — Why SAFe/Agile vocabulary was rejected. Specific structural hiding patterns. */
public class AntiPatternCatalogue implements L3_Spec<CulturalStudies> {
    public static final AntiPatternCatalogue INSTANCE = new AntiPatternCatalogue();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Jarista does not optimize Agile. It rejects the premise.")
                .ln("'Goal', 'Epic', 'Feature' are SAFe vocabulary —")
                .ln("borrowed authority that obscures instead of clarifying.")
                .ln("Jarista uses L1, L2, L3: depth is the only truth.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Traditional engineering says 'that's not a deliverable'")
                .ln("about anything it can't put in a Sprint ticket.")
                .ln("But that IS the 南郭 thinking — if it doesn't fit the")
                .ln("ceremony, it doesn't exist. Jarista rejects that too.")
            .as(RATIONALE),

            _db.text()
                .ln("Hiding patterns this branch documents:")
                .ln("• Dashboard green-washing — all statuses green, project failing")
                .ln("• Vocabulary theatre — SAFe terms used without SAFe understanding")
                .ln("• Ceremony compliance — standups held, no actual communication")
                .ln("• Metric gaming — velocity optimized, value not delivered")
            .as(NOTE)
        );
    }
}
