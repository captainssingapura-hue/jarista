package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — 虽猫可代之: even a cat can replace the faker once the structure is clear.
 */
public class CartoonTamaCat implements L4_Spec<NanguoAnalogy>, LoadBearing {
    public static final CartoonTamaCat INSTANCE = new CartoonTamaCat();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("虽猫可代之 — even a cat can replace them.")
                .ln("Once the 南郭s flee and the structure is clear,")
                .ln("the work itself is not mysterious. A tama cat,")
                .ln("following the graph, can do what the faker pretended to.")
            .as(DESCRIPTION),

            _db.text()
                .ln("This is not an insult to the work. It is a compliment")
                .ln("to the structure. When a project is well-decomposed,")
                .ln("each piece is small enough that competence — not heroism —")
                .ln("is sufficient. The cat is the proof that the graph works.")
            .as(RATIONALE),

            _db.svg("cartoons/tama-cat.svg")
            .as(PRODUCT)
        );
    }
}
