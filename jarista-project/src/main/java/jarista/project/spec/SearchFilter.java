package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;
import static jarista.detail.ResourceType.MARKDOWN;

/** L3 — Search and filter nodes in the tree by name, level, or type. */
public class SearchFilter implements L3_Spec<UILayer>, LoadBearing {
    public static final SearchFilter INSTANCE = new SearchFilter();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Real-time search bar that filters the tree view by node name,")
                .ln("spec level, or type (spec vs piece).")
                .ln("Matching nodes stay visible with their ancestor path.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Typing in the search bar instantly narrows the tree.")
                .ln("Clearing the search restores the full tree.")
                .ln("Keyboard shortcut (Ctrl+F) focuses the search bar.")
            .as(EXPECTATION),

            _db.resource("docs/ui/search-filter.md", MARKDOWN)
            .as(REFERENCE)
        );
    }
}
