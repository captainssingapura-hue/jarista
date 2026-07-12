package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Barista Brew cartoon: raw beans become a structured graph.
 */
public class CartoonBaristaBrew implements L4_Spec<BaristaIdentity>, LoadBearing {
    public static final CartoonBaristaBrew INSTANCE = new CartoonBaristaBrew();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("A barista turns raw beans into something people actually want.")
                .ln("Jarista turns raw Java classes into a validated project graph.")
            .as(DESCRIPTION),

            _db.svg("cartoons/barista-brew.svg")
            .as(PRODUCT)
        );
    }
}
