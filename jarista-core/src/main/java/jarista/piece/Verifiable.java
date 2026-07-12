package jarista.piece;

/**
 * Optional interface for pieces with a concrete verification target.
 *
 * <p>An agent completes the work, runs the verification, confirms green,
 * then marks {@code done()=true}. Self-verifying work — closes the loop
 * without human review for routine tasks.
 */
public interface Verifiable {

    /**
     * Test class, method, or command that validates this piece's completion.
     * Empty string means no automated verification (rely on EXPECTATION details).
     *
     * @return verification target, e.g. "my.project.FooTest#testBar" or "mvn verify -pl module"
     */
    default String verificationTarget() {
        return "";
    }
}
