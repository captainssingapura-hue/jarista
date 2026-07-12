package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Invisible Infrastructure: essential work that produces no artifact.
 *
 * <p>Shared mental models, design philosophy, cultural alignment,
 * theoretical foundations — the nervous system of a project.
 * Invisible but load-bearing. Remove it and the organism dies
 * slowly, from the inside, in ways no post-mortem can diagnose.
 */
public class InvisibleInfrastructure implements L4_Spec<DeliverableFallacy>, LoadBearing {
    public static final InvisibleInfrastructure INSTANCE = new InvisibleInfrastructure();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("A team's most valuable asset is not its code — it is the")
                .ln("shared understanding of WHY the code is shaped the way it is.")
                .ln("")
                .ln("This understanding lives in no artifact. It exists in the")
                .ln("space between people: in design discussions that were never")
                .ln("recorded, in whiteboard sessions that were erased, in the")
                .ln("intuition that senior engineers carry and juniors absorb")
                .ln("through proximity — until the seniors leave.")
                .ln("")
                .ln("The 'not a deliverable' reflex ensures this understanding")
                .ln("is never formalized, never typed, never validated.")
                .ln("It remains oral tradition in an industry that prides itself")
                .ln("on version control.")
            .as(DESCRIPTION),

            _db.text()
                .ln("A new team member joins. The codebase has 200k lines.")
                .ln("The README says 'run mvn install'. The wiki is three years stale.")
                .ln("The design decisions live in Slack threads that were never indexed.")
                .ln("")
                .ln("This is not a failure of documentation tools.")
                .ln("It is the predictable outcome of a culture that says")
                .ln("'documentation is not a deliverable.'")
            .as(EXAMPLE),

            _db.text()
                .ln("Jarista's Detail ADT + Resource linking is the antidote.")
                .ln("The understanding travels WITH the node — typed, compiled,")
                .ln("validated. When the spec moves, its rationale moves.")
                .ln("When the spec is deleted, its rationale is deleted.")
                .ln("No orphaned wiki pages. No stale Confluence.")
                .ln("The understanding is infrastructure, and Jarista makes it visible.")
            .as(DESIGN)
        );
    }
}
