package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.StateInterfaces;

/** Implement StateStore, PieceStatus (sealed ADT), Freshness (record). */
public class ImplStateInterfaces implements L1_Piece<StateInterfaces> {
    public static final ImplStateInterfaces INSTANCE = new ImplStateInterfaces();

    @Override public boolean done() { return true; }
}
