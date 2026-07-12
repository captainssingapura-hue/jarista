package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — "It is not a deliverable" — the most invisible anti-pattern.
 *
 * <p>The reflex to dismiss anything that doesn't look like shipped code.
 * Cultural grounding, theoretical foundations, design philosophy,
 * deep documentation — all erased because they don't fit the
 * delivery-centric worldview inherited from industrial manufacturing.
 *
 * <p>南郭 in reverse: one fakes the output, the other erases the input.
 * Both worship the appearance of delivery over the substance of work.
 */
public class DeliverableFallacy implements L3_Spec<CulturalStudies> {
    public static final DeliverableFallacy INSTANCE = new DeliverableFallacy();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("The statement 'it is not a deliverable' is never a neutral observation.")
                .ln("It is a value judgment disguised as a category claim. It says:")
                .ln("what you propose has no value — because I cannot see it ship.")
                .ln("")
                .ln("This reflex operates at every level:")
                .ln("  - Sprint planning: 'we can't put cultural studies in the backlog'")
                .ln("  - Code review: 'this documentation doesn't need its own PR'")
                .ln("  - Architecture: 'the math can go in a wiki somewhere'")
                .ln("  - Team norms: 'what did you ship this week?'")
                .ln("")
                .ln("The hierarchy is never stated but universally enforced:")
                .ln("  Shipped code        → real work")
                .ln("  Tests               → tolerated")
                .ln("  Documentation       → grudging")
                .ln("  Design philosophy   → 'just talking'")
                .ln("  Cultural grounding  → 'not work at all'")
            .as(DESCRIPTION),

            _db.text()
                .ln("Industrial manufacturing: output = value.")
                .ln("A factory that produces no widgets produces no value.")
                .ln("Software inherited this equation without questioning it.")
                .ln("")
                .ln("But software is not a factory. A heuristic without its")
                .ln("mathematical foundation is a 南郭 pretending to play.")
                .ln("The equation IS the deliverable. The diagram IS the deliverable.")
                .ln("The cultural study IS the deliverable.")
                .ln("")
                .ln("What makes this fallacy especially insidious: the people")
                .ln("who enforce it genuinely believe they are being practical.")
                .ln("They are not villains — they are products of a system that")
                .ln("trained them to see only one shape of value.")
            .as(RATIONALE),

            _db.text()
                .ln("Jarista's type system is the structural rebuttal.")
                .ln("CulturalStudies is L2, equal to CoreTypeSystem.")
                .ln("The graph does not ask 'is this a deliverable?'")
                .ln("It asks 'is this a node?' If it compiles, it exists.")
                .ln("If it validates, it matters. The type system itself")
                .ln("refuses to encode the hierarchy that the fallacy demands.")
            .as(DESIGN),

            _db.text()
                .ln("In the notation project, drum density analysis uses probability")
                .ln("distributions. Without the math, the code is a black box.")
                .ln("'Ship the code, document later' means 'never document.'")
                .ln("The heuristic works until someone needs to modify it,")
                .ln("and then the absent equation becomes the most expensive")
                .ln("missing deliverable in the project's history.")
            .as(EXAMPLE)
        );
    }
}
