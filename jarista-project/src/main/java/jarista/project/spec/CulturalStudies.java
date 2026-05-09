package jarista.project.spec;

import jarista.Stateless;
import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L2_Spec;

import java.util.List;
import java.util.Set;

import static jarista.detail.DetailRole.*;

/**
 * L2 — The philosophical and cultural dimension of Jarista.
 * Why this tool exists, not just what it does.
 */
public class CulturalStudies implements L2_Spec<JaristaGoal> {
    public static final CulturalStudies INSTANCE = new CulturalStudies();

    public Set<Stateless> dependencies() {
        return Set.of(CoreTypeSystem.INSTANCE);
    }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Jarista's real differentiation is philosophical, not technical.")
                .ln("Any team can build a graph validator.")
                .ln("What makes Jarista Jarista is the insight that structure should")
                .ln("expose fakers, that the graph is a readable score, and that")
                .ln("traditional PM is accountability theater.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Strip the cultural layer out, and you have a generic classpath scanner.")
                .ln("This branch ensures the ideas that give the tool meaning")
                .ln("receive the same structural rigor as the code that implements them.")
            .as(RATIONALE)
        );
    }
}
