package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.ClasspathScanning;

/** Implement GraphScanner using ClassGraph to discover all Stateless classes. */
public class ImplScanner implements L1_Piece<ClasspathScanning> {
    public static final ImplScanner INSTANCE = new ImplScanner();

    @Override public boolean done() { return true; }
}
