package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.CycleDetection;

/** Implement CycleDetector — DFS with back-edge detection on dependencies(). */
public class ImplCycleDetector implements L1_Piece<CycleDetection> {
    public static final ImplCycleDetector INSTANCE = new ImplCycleDetector();

    @Override public boolean done() { return true; }
}
