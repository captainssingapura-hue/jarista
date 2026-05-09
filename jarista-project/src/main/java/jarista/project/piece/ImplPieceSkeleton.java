package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.PieceSkeleton;

/** Implement L1_Piece through L8_Piece with generic parent bounds. */
public class ImplPieceSkeleton implements L1_Piece<PieceSkeleton> {
    public static final ImplPieceSkeleton INSTANCE = new ImplPieceSkeleton();
}
