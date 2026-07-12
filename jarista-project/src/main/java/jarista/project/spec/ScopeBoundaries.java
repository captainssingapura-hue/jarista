package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Scoped interface: explicit boundaries for safe autonomy.
 *
 * <p>The {@code outOfScope()} method tells a worker (especially an agent)
 * what NOT to touch. Guardrails that make autonomy safe — the more
 * freedom the agent has, the more important the fences become.
 */
public class ScopeBoundaries implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final ScopeBoundaries INSTANCE = new ScopeBoundaries();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Scoped interface on PieceBase:")
                .ln("  default List<String> outOfScope() { return List.of(); }")
                .ln("")
                .ln("Examples:")
                .ln("  'Do not modify public API signatures'")
                .ln("  'Tests only — no production code changes'")
                .ln("  'Stay within the notation-core module'")
                .ln("  'Do not change the parser; only add new strategies'")
            .as(DESIGN),

            _db.text()
                .ln("In human teams, scope is communicated verbally or assumed.")
                .ln("In agentic development, unspoken assumptions become bugs.")
                .ln("An agent that over-reaches is worse than one that under-delivers —")
                .ln("it creates work for the reviewer and erodes trust in automation.")
                .ln("outOfScope() makes the implicit explicit.")
            .as(RATIONALE)
        );
    }
}
