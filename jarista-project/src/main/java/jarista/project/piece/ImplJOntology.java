package jarista.project.piece;

import jarista.piece.L1_Piece;
import jarista.project.spec.JOntologyIntegration;

/** Wire jarista.Stateless to extend hue.captains.singapura.tao.ontology.Stateless. */
public class ImplJOntology implements L1_Piece<JOntologyIntegration> {
    public static final ImplJOntology INSTANCE = new ImplJOntology();
}
