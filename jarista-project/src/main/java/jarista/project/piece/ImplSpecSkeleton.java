package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.SpecSkeleton;

/** Implement L1_Spec through L18_Spec with generic parent bounds. */
public class ImplSpecSkeleton implements L1_Piece<SpecSkeleton> {
    public static final ImplSpecSkeleton INSTANCE = new ImplSpecSkeleton();

    @Override public boolean done() { return true; }
}
