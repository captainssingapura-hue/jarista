package jarista;

import jarista.detail.RoledDetail;

import java.util.List;

public interface Stateless extends hue.captains.singapura.tao.ontology.Stateless {

    /**
     * Typed detail blocks describing this node — description, expectations,
     * examples, references, etc.  Override to provide content; the default
     * is an empty list.
     */
    default List<RoledDetail> details() {
        return List.of();
    }
}
