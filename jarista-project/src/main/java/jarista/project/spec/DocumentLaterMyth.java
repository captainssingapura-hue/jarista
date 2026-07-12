package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — "We'll document later" — the terminal form of the fallacy.
 *
 * <p>The promise that deferred documentation will eventually be written.
 * It never is. 'Later' is a polite way of saying 'never' in a culture
 * where only delivery counts. The debt compounds silently until a
 * critical team member leaves and takes the understanding with them.
 */
public class DocumentLaterMyth implements L4_Spec<DeliverableFallacy>, LoadBearing {
    public static final DocumentLaterMyth INSTANCE = new DocumentLaterMyth();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("'Document later' is the most common lie in software engineering.")
                .ln("Not because engineers are dishonest — but because the system")
                .ln("never creates space for 'later.' Every sprint has new features.")
                .ln("Every quarter has new priorities. The backlog grows forward.")
                .ln("Documentation debt has no advocate, no champion, no urgency —")
                .ln("until the crisis arrives and there is no time to write it then either.")
            .as(DESCRIPTION),

            _db.text()
                .ln("The myth has a lifecycle:")
                .ln("  1. Feature ships. 'We'll document it next sprint.'")
                .ln("  2. Next sprint has its own features. 'After this release.'")
                .ln("  3. After release: 'We need to focus on the next milestone.'")
                .ln("  4. Original author leaves. Knowledge evaporates.")
                .ln("  5. New team reverse-engineers the code. Introduces bugs.")
                .ln("  6. Post-mortem: 'We need better documentation practices.'")
                .ln("  7. Go to step 1.")
            .as(EXAMPLE),

            _db.text()
                .ln("The fix is not discipline — it is structure.")
                .ln("When documentation is a typed Detail attached to the node,")
                .ln("it is not a separate task that can be deferred.")
                .ln("The spec IS incomplete without its details.")
                .ln("A LoadBearing spec returning DONE with empty details()")
                .ln("is as suspicious as a test with no assertions.")
                .ln("The type system makes the absence visible.")
            .as(RATIONALE)
        );
    }
}
