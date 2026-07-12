package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/** L3 — 结图为谱: weave the graph into a score. The notation-as-structure principle. */
public class ScoreMetaphor implements L3_Spec<CulturalStudies>, LoadBearing {
    public static final ScoreMetaphor INSTANCE = new ScoreMetaphor();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("结图为谱 — weave the graph into a score.")
                .ln("A musical score is not a description of music. It IS the music,")
                .ln("in a form that is both human-readable and machine-executable.")
                .ln("Jarista's graph is the same: not a description of the project,")
                .ln("but the project structure itself, compiled and validated.")
            .as(DESCRIPTION),

            _db.text()
                .ln("The woven crosshatch in the icon is a literal visual of this —")
                .ln("threads crossing to form a fabric. The graph is the weave.")
                .ln("Each spec is a thread. The pattern reveals gaps.")
            .as(DESIGN)
        );
    }
}
