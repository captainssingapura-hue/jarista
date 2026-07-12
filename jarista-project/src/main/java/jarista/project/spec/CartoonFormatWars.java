package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — The Format Wars cartoon: two painters arguing about brushes
 * while the canvas asks what the painting is of.
 */
public class CartoonFormatWars implements L3_Spec<ContentManagement>, LoadBearing {
    public static final CartoonFormatWars INSTANCE = new CartoonFormatWars();
    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Two painters argue furiously about brushes (Markdown vs HTML)")
                .ln("while the blank canvas behind them asks: 'But what AM I?'")
                .ln("A third figure (Jarista) quietly paints with typed content,")
                .ln("ignoring the argument entirely.")
            .as(DESCRIPTION),

            _db.svg("cartoons/format-wars.svg")
            .as(PRODUCT)
        );
    }
}
