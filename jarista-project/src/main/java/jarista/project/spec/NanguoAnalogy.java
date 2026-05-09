package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/** L3 — The 南郭先生 story: its mapping to PM, why it's the core diagnostic. */
public class NanguoAnalogy implements L3_Spec<CulturalStudies> {
    public static final NanguoAnalogy INSTANCE = new NanguoAnalogy();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("南郭先生 — Mr. Nanguo pretended to play the yu in King Xuan's")
                .ln("300-man ensemble. When King Min preferred solos, Nanguo fled.")
                .ln("The ensemble hid the faker; the solo exposed him.")
            .as(DESCRIPTION),

            _db.text()
                .ln("In project management, dashboards and status updates are")
                .ln("the 300-man ensemble. Everyone reports green.")
                .ln("Jarista is the solo — compiled structure that cannot be faked.")
                .ln("If your spec has no piece, the graph says so. No hiding.")
            .as(RATIONALE),

            _db.text()
                .ln("Every node in the graph is a potential solo.")
                .ln("The Nanguo Report surfaces bare specs — the silent instruments.")
                .ln("This is not punitive. It is clarifying.")
            .as(EXPECTATION)
        );
    }
}
