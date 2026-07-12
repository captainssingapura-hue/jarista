package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L3 — Verifiable interface: how to check if work is done.
 *
 * <p>The {@code verificationTarget()} method points to a test or command
 * that validates completion. An agent completes work, runs verification,
 * confirms green, then marks {@code done()=true}. Self-verifying work.
 */
public class SelfVerification implements L3_Spec<WorkflowImprovement>, LoadBearing {
    public static final SelfVerification INSTANCE = new SelfVerification();

    @Override public SpecStatus status() { return SpecStatus.DONE; }

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Verifiable interface on PieceBase:")
                .ln("  default String verificationTarget() { return \"\"; }")
                .ln("")
                .ln("Concrete pointer to how to verify:")
                .ln("  'my.project.FooTest#testBar'")
                .ln("  'mvn verify -pl notation-core'")
                .ln("  'run GraphValidator on music.notation.jarista.spec'")
            .as(DESIGN),

            _db.text()
                .ln("Closes the loop: work → verify → mark done → commit.")
                .ln("For routine tasks, no human review needed.")
                .ln("For significant changes, the commit still goes through")
                .ln("normal review — but the agent already knows its work passes.")
            .as(RATIONALE),

            _db.text()
                .ln("When verificationTarget() is empty, fall back to EXPECTATION")
                .ln("details — prose description of what 'done' looks like.")
                .ln("Not all work is test-verifiable. Cultural studies, design")
                .ln("documents, theoretical foundations require human judgment.")
                .ln("The interface accommodates both: automated and manual.")
            .as(NOTE)
        );
    }
}
