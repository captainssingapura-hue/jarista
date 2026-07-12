package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Graph as agent coordination layer.
 *
 * <p>Multiple agents (or human+agent pairs) work on different pieces
 * under the same spec. No standup, no Jira board, no Slack thread.
 * The graph shows what's done, what's open, what depends on what.
 * An agent picks the next open piece, reads its details, works.
 */
public class AgenticCoordination implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final AgenticCoordination INSTANCE = new AgenticCoordination();

    @Override public SpecStatus status() { return SpecStatus.IN_PROGRESS; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("The agentic workflow:")
                .ln("  1. Agent receives context: 'work on this project'")
                .ln("  2. Agent scans graph: finds pieces with done()=false")
                .ln("  3. Agent reads piece details: scope, expectations, constraints")
                .ln("  4. Agent checks outOfScope(): understands boundaries")
                .ln("  5. Agent does the work")
                .ln("  6. Agent runs verificationTarget(): confirms green")
                .ln("  7. Agent sets done()=true, commits")
                .ln("  8. Graph validator confirms consistency")
                .ln("  9. Report generator shows updated status")
            .as(DESIGN),

            _db.text()
                .ln("Multiple agents, same spec — parallel work without overhead:")
                .ln("")
                .ln("  MxlParsing (spec, IN_PROGRESS)")
                .ln("    ParseBasicElements  (piece, done=true)   ← Agent A")
                .ln("    HandleVoiceSep      (piece, done=false)  ← Agent B working")
                .ln("    TestEdgeCases       (piece, done=false)  ← next agent picks up")
                .ln("    DocumentHeuristics  (piece, done=false)  ← human will do this")
                .ln("")
                .ln("No standup needed. The graph IS the coordination.")
            .as(EXAMPLE),

            _db.text()
                .ln("Traditional coordination: plan → assign → track → review → blame.")
                .ln("Graph coordination: reveal → discover → act → verify → evidence.")
                .ln("")
                .ln("The graph does not command. It reveals.")
                .ln("The agent does not report. It commits.")
                .ln("The status is not curated. It is derived.")
            .as(RATIONALE)
        );
    }
}
