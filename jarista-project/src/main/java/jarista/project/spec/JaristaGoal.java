package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L1_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L1 — The root.
 * Make project management easy and relaxed.
 * Make professional project managers obsolete.
 */
public class JaristaGoal implements L1_Spec {
    public static final JaristaGoal INSTANCE = new JaristaGoal();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Graph-native, repo-resident project management for Java.")
                .ln("No YAML, no dashboards, no accountability theater.")
                .ln("The graph lives in compiled code — if it compiles, the structure is valid.")
            .as(DESCRIPTION),

            _db.text()
                .ln("结图为谱，南郭立现。")
                .ln("Weave the graph into a score — the fakers reveal themselves.")
                .ln("Traditional PM tools let people hide behind status updates.")
                .ln("Jarista makes the structure machine-checkable.")
            .as(RATIONALE),

            _db.text()
                .ln("A single 'mvn verify' validates the entire project graph.")
                .ln("Every spec and piece is a compiled Java class.")
                .ln("Cycles, orphans, and missing pieces are caught at build time.")
            .as(EXPECTATION)
        );
    }
}
