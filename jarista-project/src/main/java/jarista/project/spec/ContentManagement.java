package jarista.project.spec;

import jarista.Stateless;
import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L2_Spec;

import java.util.List;
import java.util.Set;

import static jarista.detail.DetailRole.*;

/**
 * L2 — Typed content system for node documentation.
 *
 * <p>Essential details live in strongly-typed Java (Detail ADT + roles).
 * Detailed explanations, diagrams, math equations, and rich media
 * are linked as static resources — keeping the skeleton in code
 * and the flesh in the format that suits it best.
 */
public class ContentManagement implements L2_Spec<JaristaGoal> {
    public static final ContentManagement INSTANCE = new ContentManagement();

    private static final DetailBuilder _db = new DetailBuilder();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE);
    }

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Two layers of documentation per node:")
                .ln("1. Skeleton — typed Java: Detail ADT + semantic roles + fluent builder.")
                .ln("   Essential shape, always compiled, always in sync.")
                .ln("2. Flesh — static resources: markdown, HTML, SVG, PNG, LaTeX, PDF.")
                .ln("   Rich detail that lives in the format native to the content.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Heuristics in a notation system need diagrams to be understood")
                .ln("and math equations to be theoretically sound. A project management")
                .ln("tool that cannot host this content forces it into wikis and shared")
                .ln("drives — severing the link between spec and explanation.")
                .ln("The node IS the single source of truth; its resources travel with it.")
            .as(RATIONALE)
        );
    }
}
