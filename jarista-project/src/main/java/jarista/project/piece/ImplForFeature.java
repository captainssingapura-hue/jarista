package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.FeatureAnnotation;

/** Implement @ForFeature with @Retention(RUNTIME), @Target(TYPE, METHOD). */
public class ImplForFeature implements L1_Piece<FeatureAnnotation> {
    public static final ImplForFeature INSTANCE = new ImplForFeature();

    @Override public boolean done() { return true; }
}
