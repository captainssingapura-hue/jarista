package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — Plan to Blame: when the plan becomes a weapon.
 *
 * <p>The deliverable fallacy, institutionalized. The plan tracks only
 * artifacts. Anything invisible to the plan has no protection.
 * When the missing understanding causes failure, the plan assigns
 * blame to the individual — never to the system that made
 * understanding invisible in the first place.
 *
 * <p>The plan does not enable work. It documents expectations
 * so that deviation can be punished. This is not project management.
 * It is premeditated accountability theater.
 */
public class PlanToBlame implements L4_Spec<DeliverableFallacy>, LoadBearing {
    public static final PlanToBlame INSTANCE = new PlanToBlame();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("The anatomy of plan-to-blame:")
                .ln("")
                .ln("1. The plan is created. It lists deliverables and dates.")
                .ln("   Cultural studies, theoretical foundations, design depth")
                .ln("   are absent — 'not deliverables.'")
                .ln("")
                .ln("2. The team works. Some spend time on understanding —")
                .ln("   the math behind the heuristic, the structural reason")
                .ln("   for a design choice. This work is invisible to the plan.")
                .ln("")
                .ln("3. The sprint ends. The plan shows red. Understanding-work")
                .ln("   produced no trackable artifact. The velocity chart dips.")
                .ln("")
                .ln("4. The retrospective: 'Why did we miss the target?'")
                .ln("   The plan answers: these items were not completed.")
                .ln("   The plan does not answer: the items that WERE completed")
                .ln("   were only possible because of the understanding that")
                .ln("   the plan refused to see.")
                .ln("")
                .ln("5. Blame is assigned. Not to the plan. To the people.")
            .as(DESCRIPTION),

            _db.text()
                .ln("The plan is never neutral. It encodes a theory of value.")
                .ln("When that theory says only artifacts count, the plan becomes")
                .ln("a machine for converting invisible work into visible blame.")
                .ln("")
                .ln("Estimation becomes commitment. 'We estimated 5 points'")
                .ln("becomes 'you committed to 5 points.' The linguistic shift")
                .ln("is deliberate — estimation implies uncertainty; commitment")
                .ln("implies culpability.")
                .ln("")
                .ln("The Gantt chart is not a timeline. It is a blame map.")
                .ln("Each bar is a promise. Each slip is an accusation.")
                .ln("The chart does not ask 'what did we learn?' —")
                .ln("it asks 'who failed?'")
            .as(RATIONALE),

            _db.text()
                .ln("The 南郭 connection is structural, not metaphorical.")
                .ln("")
                .ln("南郭's ensemble checks attendance, not musicianship.")
                .ln("Plan-to-blame checks delivery, not understanding.")
                .ln("Both systems are optimized for one thing:")
                .ln("making the absence of substance undetectable —")
                .ln("until the solo arrives.")
                .ln("")
                .ln("When the solo arrives (the crisis, the departed expert,")
                .ln("the edge case no one documented), the system collapses")
                .ln("and blame flows to the nearest individual.")
                .ln("The system itself is never indicted.")
            .as(REFERENCE),

            _db.text()
                .ln("Jarista's graph is not a plan. It is a structure.")
                .ln("")
                .ln("A plan says 'deliver X by Y or explain why not.'")
                .ln("A graph says 'X exists, depends on Y, status is Z.'")
                .ln("")
                .ln("There is no timeline. There is no commitment date.")
                .ln("There is no velocity. There is only structure and status.")
                .ln("A node is PLANNED, IN_PROGRESS, or DONE — declared by")
                .ln("the spec author, validated by pieces, derived for parents.")
                .ln("")
                .ln("The graph cannot blame because it has no expectations.")
                .ln("It can only reveal — what exists, what is missing,")
                .ln("what depends on what. Revelation without judgment.")
                .ln("The opposite of plan-to-blame.")
            .as(DESIGN)
        );
    }
}
