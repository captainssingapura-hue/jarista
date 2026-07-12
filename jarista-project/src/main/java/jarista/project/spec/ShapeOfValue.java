package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Shape of Value: why only one shape is recognized.
 *
 * <p>Engineering culture recognizes exactly one shape of value:
 * the shipped artifact. Everything else is overhead, process,
 * ceremony. This is not malice — it is a trained blindness
 * inherited from manufacturing and reinforced by every tool,
 * metric, and ritual in the modern software process.
 */
public class ShapeOfValue implements L4_Spec<DeliverableFallacy>, LoadBearing {
    public static final ShapeOfValue INSTANCE = new ShapeOfValue();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Ask an engineer what they accomplished this week.")
                .ln("The answer is always in artifact form:")
                .ln("  'I shipped the auth module.'")
                .ln("  'I fixed 12 bugs.'")
                .ln("  'I merged three PRs.'")
                .ln("")
                .ln("No one says:")
                .ln("  'I understood why the tempo model diverges at extreme BPMs.'")
                .ln("  'I discovered that our chord voicing algorithm assumes")
                .ln("   equal temperament, which breaks for historical tunings.'")
                .ln("  'I built a mental model of the phrase tree that will")
                .ln("   prevent six wrong design decisions next quarter.'")
                .ln("")
                .ln("The second set is more valuable. But it has no shape")
                .ln("that the culture recognizes as 'work.'")
            .as(DESCRIPTION),

            _db.text()
                .ln("Manufacturing: value = output / time.")
                .ln("Consulting: value = billable hours × rate.")
                .ln("Software: value = ... features shipped?")
                .ln("")
                .ln("The equation was never examined. It was inherited.")
                .ln("Software adopted Scrum (from manufacturing), sprints (from")
                .ln("athletics), velocity (from physics). All metaphors for")
                .ln("linear output. None for understanding, depth, or growth.")
                .ln("")
                .ln("A living organism's value is not its output.")
                .ln("It is its capacity to adapt, to heal, to grow.")
                .ln("That capacity comes from understanding — the one thing")
                .ln("the delivery-centric model systematically erases.")
            .as(RATIONALE),

            _db.text()
                .ln("Jarista recognizes multiple shapes of value by refusing")
                .ln("to rank them. L2_Spec<JaristaGoal> is the only type")
                .ln("constraint — whether the content is code infrastructure,")
                .ln("cultural studies, or content management. The graph is flat")
                .ln("where the industry insists on a hierarchy.")
            .as(DESIGN)
        );
    }
}
