package jarista.project.link;

import jarista.annotation.ForFeature;
import jarista.link.ImplementationFor;
import jarista.link.Linkage;
import jarista.piece.L1_Piece;
import jarista.project.spec.FeatureAnnotation;
import jarista.project.spec.ImplementationLinkage;
import jarista.project.spec.PieceSkeleton;
import jarista.project.spec.SpecSkeleton;
import jarista.project.spec.StateInterfaces;
import jarista.spec.L1_Spec;
import jarista.state.StateStore;

import java.util.List;

/**
 * Jarista's own linkage — the self-hosted dogfood of {@link ImplementationFor}.
 *
 * <p>This class is the only place that names both an implementation class and
 * the spec it fulfils. The core classes linked here know nothing of
 * {@code jarista.project}, and the specs know nothing of the classes.
 */
public final class JaristaLinkage implements Linkage {

    public static final JaristaLinkage INSTANCE = new JaristaLinkage();

    private JaristaLinkage() {}

    @Override
    public List<ImplementationFor<?, ?>> links() {
        return List.of(
                ImplementationFor.of(ImplementationFor.class, ImplementationLinkage.INSTANCE),
                ImplementationFor.of(ForFeature.class,        FeatureAnnotation.INSTANCE),
                ImplementationFor.of(L1_Spec.class,           SpecSkeleton.INSTANCE),
                ImplementationFor.of(L1_Piece.class,          PieceSkeleton.INSTANCE),
                ImplementationFor.of(StateStore.class,        StateInterfaces.INSTANCE)
        );
    }
}
