package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.DetailRole;
import jarista.detail.ResourceType;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Nanguo Solo cartoon: Mr. Nanguo exposed when the ensemble becomes a solo.
 */
public class CartoonNanguoSolo implements L4_Spec<NanguoAnalogy>, LoadBearing {
    public static final CartoonNanguoSolo INSTANCE = new CartoonNanguoSolo();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("南郭先生 hid in the 300-man orchestra. When the king demanded solos,")
                .ln("he fled. This cartoon illustrates the moment of exposure.")
            .as(DESCRIPTION),

            _db.svg("cartoons/nanguo-solo.svg")
            .as(PRODUCT)
        );
    }
}
