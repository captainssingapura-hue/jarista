package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Auto-generated project reports from the graph.
 *
 * <p>No database. No API. Compile, scan, render.
 * The report IS the project status, derived from the same source
 * as the code. CI/CD runs it on every commit.
 * The dashboard no one forgets to update — because there is
 * no dashboard to update.
 */
public class AutoReporting implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final AutoReporting INSTANCE = new AutoReporting();

    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Web report generated from the graph on every build:")
                .ln("")
                .ln("  Overview         → spec/piece counts, completion %")
                .ln("  Open TODOs       → pieces where done()=false, grouped by spec")
                .ln("  Completed work   → pieces where done()=true")
                .ln("  Status breakdown → LoadBearing specs by PLANNED / IN_PROGRESS / DONE")
                .ln("  Dependency map   → graph visualization")
                .ln("  Detail content   → markdown rendered, SVG embedded, code highlighted")
                .ln("  Timeline         → git log on piece files (when was done() flipped?)")
            .as(DESIGN),

            _db.text()
                .ln("The report serves three audiences with one artifact:")
                .ln("  Team      → spec status roll-ups, what's in progress")
                .ln("  Workers   → open pieces with full details, next actions")
                .ln("  Observers → high-level progress without task noise")
                .ln("")
                .ln("No one curates this. It is always current because")
                .ln("it is always derived. The graph is the single source.")
            .as(RATIONALE)
        );
    }
}
