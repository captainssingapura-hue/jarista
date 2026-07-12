package jarista.project.spec;

import jarista.Stateless;
import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L2_Spec;

import java.util.List;
import java.util.Set;

import static jarista.detail.DetailRole.*;

/**
 * L2 — Workflow improvement: how Jarista helps people and agents work better.
 *
 * <p>A never-ending journey. The goal is not to ship a workflow engine —
 * it is to continuously improve how the graph enables work.
 * Specs define what the project needs (for the team).
 * Pieces define what to do next (for the worker).
 * The graph coordinates without commanding.
 *
 * <p>Especially important in fast-paced AI-assisted and agentic development,
 * where the "worker" is an agent that needs structured, self-contained,
 * verifiable work orders — not tickets designed for standup rituals.
 */
public class WorkflowImprovement implements L2_Spec<JaristaGoal> {
    public static final WorkflowImprovement INSTANCE = new WorkflowImprovement();

    private static final DetailBuilder _db = new DetailBuilder();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE, ContentManagement.INSTANCE);
    }

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Two audiences, one graph:")
                .ln("")
                .ln("  Spec  → for the team.  'This capability must exist.'")
                .ln("  Piece → for the worker. 'Here is a concrete task.'")
                .ln("")
                .ln("Ticket systems collapse both into one artifact, then dress it")
                .ln("with fields that serve neither: story points for managers,")
                .ln("priority for triage, assignee for blame, due date for pressure.")
                .ln("")
                .ln("Jarista separates them. The spec stays abstract and long-lived.")
                .ln("The piece is born, worked, completed. It doesn't evolve —")
                .ln("it flips from false to true. But it stays in the graph as")
                .ln("evidence: this work happened.")
            .as(DESCRIPTION),

            _db.text()
                .ln("In agentic development, the piece IS the agent's instruction set.")
                .ln("A well-written piece with DESCRIPTION, EXPECTATION, DESIGN,")
                .ln("REFERENCE, and outOfScope() is everything an agent needs.")
                .ln("The agent discovers open pieces by scanning done()=false,")
                .ln("reads details, acts, verifies, marks done, commits.")
                .ln("The commit is the completion event. The graph is the coordinator.")
            .as(RATIONALE),

            _db.text()
                .ln("This is a never-ending L2. Software workflow is a living practice.")
                .ln("New sub-specs will emerge as we learn what helps and what doesn't.")
                .ln("The graph grows with the practice — not ahead of it.")
            .as(NOTE)
        );
    }
}
