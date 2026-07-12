package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — L1_Piece through L8_Piece with generic parent constraints. */
public class PieceSkeleton implements L3_Spec<CoreTypeSystem>, LoadBearing {
    public static final PieceSkeleton INSTANCE = new PieceSkeleton();
    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
