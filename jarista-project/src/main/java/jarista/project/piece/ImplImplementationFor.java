package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.ImplementationLinkage;

/** Implement ImplementationFor<C, S extends Spec>, Linkage, the Spec marker, and LinkageScanner. */
public class ImplImplementationFor implements L1_Piece<ImplementationLinkage> {
    public static final ImplImplementationFor INSTANCE = new ImplImplementationFor();

    @Override public boolean done() { return true; }
}
