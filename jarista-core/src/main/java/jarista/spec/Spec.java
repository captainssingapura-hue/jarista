package jarista.spec;

import jarista.Stateless;

/**
 * Marker for every spec level ({@code L0_Spec} … {@code L18_Spec}).
 *
 * <p>Pieces are {@link Stateless} too but are <em>not</em> {@code Spec}s, so a
 * type bound of {@code S extends Spec} admits specs only — the compiler rejects
 * linking an implementation to a piece or to an arbitrary node.
 */
public interface Spec extends Stateless {}
