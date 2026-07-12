package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Velocity Trap: when metrics enforce the fallacy.
 *
 * <p>Story points, sprint velocity, burndown charts — all measure
 * delivery artifacts. Work that produces understanding instead of
 * artifacts scores zero. The measurement system itself encodes
 * the bias, then the bias looks like objective truth because
 * the numbers confirm it.
 */
public class VelocityTrap implements L4_Spec<DeliverableFallacy>, LoadBearing {
    public static final VelocityTrap INSTANCE = new VelocityTrap();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Story points measure estimated effort for delivery tasks.")
                .ln("Cultural grounding, theoretical documentation, design philosophy")
                .ln("produce zero points. A team that spends a sprint building")
                .ln("deep understanding shows 'zero velocity' — and gets flagged.")
                .ln("")
                .ln("The metrics don't just reflect the fallacy; they enforce it.")
                .ln("Goodhart's Law: when a measure becomes a target, it ceases")
                .ln("to be a good measure. Velocity was meant to predict capacity.")
                .ln("It became the definition of value.")
            .as(DESCRIPTION),

            _db.text()
                .ln("A team scores 40 points shipping features without documentation.")
                .ln("Another scores 15 points but produces deep theoretical grounding")
                .ln("that prevents six months of future rework.")
                .ln("The dashboard says the first team is 2.7x more productive.")
                .ln("The dashboard is wrong — but it is never questioned,")
                .ln("because questioning the dashboard is also 'not a deliverable.'")
            .as(EXAMPLE),

            _db.text()
                .ln("Jarista has no velocity metric. The graph tracks structure,")
                .ln("not speed. A node exists or it doesn't. A spec is PLANNED,")
                .ln("IN_PROGRESS, or DONE. There is no 'points per sprint' —")
                .ln("because the graph treats a CulturalStudies node and a")
                .ln("CoreTypeSystem node with equal structural weight.")
            .as(DESIGN)
        );
    }
}
