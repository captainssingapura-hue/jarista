package jarista.detail;

/**
 * A detail block tagged with its semantic role.
 *
 * <p>{@link DetailRole} says <em>why</em> (description, expectation, …).
 * {@link Detail} says <em>what</em> (text, code, resource).
 * This record is the product type that combines both orthogonal axes.
 */
public record RoledDetail(DetailRole role, Detail detail) {}
