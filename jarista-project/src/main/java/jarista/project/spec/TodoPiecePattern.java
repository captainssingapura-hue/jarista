package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Pieces as TODO items: the worker-facing task system.
 *
 * <p>A piece with {@code done()=false} IS a TODO. Its details describe
 * what to do. Multiple pieces per spec decompose work naturally.
 * No priority field, no assignee, no story points — just structure.
 */
public class TodoPiecePattern implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final TodoPiecePattern INSTANCE = new TodoPiecePattern();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("A piece is the atomic unit of work in Jarista.")
                .ln("done()=false means 'this work is open.'")
                .ln("done()=true  means 'this work is complete.'")
                .ln("")
                .ln("Multiple pieces per spec = natural task decomposition:")
                .ln("  MxlParsing (spec, IN_PROGRESS)")
                .ln("    ParseBasicElements  (piece, done=true)")
                .ln("    HandleVoiceSep      (piece, done=false)  ← open TODO")
                .ln("    TestEdgeCases       (piece, done=false)  ← open TODO")
                .ln("    DocumentHeuristics  (piece, done=false)  ← open TODO")
            .as(DESCRIPTION),

            _db.text()
                .ln("What a TODO piece carries (via details):")
                .ln("  DESCRIPTION  → what to do")
                .ln("  EXPECTATION  → what 'done' looks like")
                .ln("  RATIONALE    → why this matters (so the worker makes good judgment calls)")
                .ln("  DESIGN       → approach hints")
                .ln("  REFERENCE    → files, modules, related specs")
                .ln("  NOTE         → constraints, gotchas, what NOT to do")
            .as(DESIGN),

            _db.text()
                .ln("What a TODO piece does NOT carry:")
                .ln("  No assignee   → git blame tells you who did it")
                .ln("  No due date   → the agent works until done")
                .ln("  No priority   → dependencies express ordering")
                .ln("  No estimate   → the piece's details describe scope")
                .ln("  No rich status → done or not done. That's it.")
            .as(RATIONALE)
        );
    }
}
