package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.InstanceEnforcement;

/** Implement InstanceEnforcer — reflective check for INSTANCE field on all Stateless classes. */
public class ImplInstanceEnforcer implements L1_Piece<InstanceEnforcement> {
    public static final ImplInstanceEnforcer INSTANCE = new ImplInstanceEnforcer();

    @Override public boolean done() { return true; }
}
