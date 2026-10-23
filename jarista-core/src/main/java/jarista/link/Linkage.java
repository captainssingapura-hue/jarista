package jarista.link;

import java.util.List;

/**
 * A declared set of {@link ImplementationFor} links — the "linkage module".
 *
 * <p>An implementation of this interface is the one place that depends on both
 * the implementation classes and the specs. It is discovered by
 * {@code LinkageScanner} and is expected to expose a
 * {@code public static final INSTANCE}, like every other Jarista node.
 *
 * <p>Deliberately <em>not</em> a {@code Stateless}: a linkage is not a node in
 * the feature graph (it has no level, no parent), so the graph scanners and
 * viewers must not pick it up.
 */
public interface Linkage {

    List<ImplementationFor<?, ?>> links();
}
