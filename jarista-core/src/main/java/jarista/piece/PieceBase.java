package jarista.piece;

import jarista.Stateless;

/**
 * Base interface for all pieces. Carries the binary done state,
 * optional scope boundaries, and optional verification target.
 *
 * <p>A piece is the worker's artifact — human or agent.
 * It says what to do ({@code details()}), whether it's done ({@code done()}),
 * what NOT to touch ({@code outOfScope()}), and how to verify ({@code verificationTarget()}).
 *
 * <p>Override {@link #done()} to return {@code true} when the work is complete.
 */
public interface PieceBase extends Stateless, Scoped, Verifiable {

    default boolean done() {
        return false;
    }
}
