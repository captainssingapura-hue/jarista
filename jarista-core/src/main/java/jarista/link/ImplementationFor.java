package jarista.link;

import jarista.spec.Spec;

import java.util.Objects;

/**
 * A non-intrusive link from an implementation class to the spec it fulfils.
 *
 * <p>This is the alternative to {@code @ForFeature}. The annotation lives
 * <em>on</em> the implementation, so implementation code must import and name
 * its spec. An {@code ImplementationFor} instead lives in a third place — a
 * {@link Linkage} — that depends on both sides, while the implementation and
 * the spec stay unaware of each other: no hard link in either direction.
 *
 * @param <C> the implementation class
 * @param <S> the spec it implements — bounded to {@link Spec}, so a piece or
 *            any other node is a compile error
 * @param implementation the implementation class (a class literal, so the link
 *                       is type-checked and IDE-navigable)
 * @param spec           the spec singleton (its {@code INSTANCE})
 */
public record ImplementationFor<C, S extends Spec>(Class<C> implementation, S spec) {

    public ImplementationFor {
        Objects.requireNonNull(implementation, "implementation");
        Objects.requireNonNull(spec, "spec");
    }

    /** Factory that reads naturally in a linkage list. */
    public static <C, S extends Spec> ImplementationFor<C, S> of(Class<C> implementation, S spec) {
        return new ImplementationFor<>(implementation, spec);
    }
}
