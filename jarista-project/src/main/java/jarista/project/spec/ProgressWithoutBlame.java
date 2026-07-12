package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Helping progress without enabling blame.
 *
 * <p>The core design constraint: every workflow feature must help
 * the worker do better work. If a feature could be used to assign
 * blame, it should not exist in Jarista.
 */
public class ProgressWithoutBlame implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final ProgressWithoutBlame INSTANCE = new ProgressWithoutBlame();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("The litmus test for any workflow feature:")
                .ln("")
                .ln("  'Could a manager use this to blame someone?'")
                .ln("")
                .ln("  done() boolean      → NO. It says what, not who or when.")
                .ln("  outOfScope()        → NO. It protects the worker, not the plan.")
                .ln("  verificationTarget()→ NO. It helps the worker self-check.")
                .ln("  details()           → NO. It enables understanding.")
                .ln("  dependencies()      → NO. It reveals structure.")
                .ln("")
                .ln("  assignee field      → YES. Remove it.")
                .ln("  due date            → YES. Remove it.")
                .ln("  story points        → YES. Remove it.")
                .ln("  priority ranking    → YES. Remove it.")
                .ln("  time tracking       → YES. Remove it.")
            .as(DESIGN),

            _db.text()
                .ln("Tracking asks: 'where are we?' (for managers)")
                .ln("Helping asks: 'what should I do next?' (for workers)")
                .ln("")
                .ln("Jarista answers the second question.")
                .ln("The first question gets answered for free —")
                .ln("derived from the same graph, never manually curated.")
                .ln("")
                .ln("The observer's view is a projection of the worker's reality.")
                .ln("Not the other way around.")
            .as(RATIONALE)
        );
    }
}
