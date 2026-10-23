package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L0_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L0 — The vision. The root above the goal.
 * Project management should be structural truth, not narrative performance.
 */
public class JaristaVision implements L0_Spec {
    public static final JaristaVision INSTANCE = new JaristaVision();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Replace ceremony with structure.")
                .ln("Project state should be something you compile, not something you perform.")
                .ln("When the plan is machine-checkable, honesty stops being a virtue and becomes a precondition.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Every PM artifact that exists to prove work — burndowns, velocity, status decks —")
                .ln("is a narrative layer bolted on because the real structure was never captured.")
                .ln("Capture the structure as typed code and the narrative layer becomes redundant.")
            .as(RATIONALE),

            _db.text()
                .ln("A world where 'the graph compiles' is the only status report anyone needs.")
                .ln("The goal (L1) and everything beneath it are one concrete expression of this vision.")
            .as(EXPECTATION)
        );
    }
}
