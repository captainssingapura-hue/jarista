package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Static resource linking via Detail.Resource.
 *
 * <p>ResourceType enum (MARKDOWN, SVG, PNG, PDF, HTML, PLAIN_TEXT)
 * tells the UI layer how to render. Path is classpath-relative,
 * co-located with the spec class or in a conventional resource directory.
 */
public class ResourceLinking implements L3_Spec<ContentManagement>, LoadBearing {
    public static final ResourceLinking INSTANCE = new ResourceLinking();

    @Override public SpecStatus status() { return SpecStatus.IN_PROGRESS; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Detail.Resource(path, type) — a reference, not embedded content.")
                .ln("The resource lives as a static file; the spec points to it.")
                .ln("Path convention: classpath-relative, e.g. 'docs/phrase-adt/overview.md'.")
            .as(DESIGN),

            _db.text()
                .ln("ResourceType drives rendering:")
                .ln("  MARKDOWN  → parsed and styled")
                .ln("  SVG / PNG → embedded image")
                .ln("  PDF       → linked or embedded viewer")
                .ln("  HTML      → rendered in web view")
                .ln("  PLAIN_TEXT → monospace block")
            .as(DESCRIPTION),

            _db.text()
                .ln("Still needed: resolution logic to locate resources on classpath,")
                .ln("convention for resource directory layout per module.")
            .as(NOTE)
        );
    }
}
