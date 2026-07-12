package jarista.graph;

import jarista.Stateless;
import jarista.spec.SpecStatus;

public sealed interface Violation {

    record MissingInstance(Class<? extends Stateless> type) implements Violation {
        @Override
        public String toString() {
            return type.getSimpleName() + " has no public static final INSTANCE field";
        }
    }

    record CyclicDependency(String cycle) implements Violation {
        @Override
        public String toString() {
            return "Cycle detected: " + cycle;
        }
    }

    record OrphanSpec(Class<? extends Stateless> type) implements Violation {
        @Override
        public String toString() {
            return type.getSimpleName() + " is not reachable from any L1_Spec root";
        }
    }

    /** A piece targets a spec that is not LoadBearing. */
    record PieceTargetsNonLeaf(Class<? extends Stateless> piece,
                               Class<? extends Stateless> spec) implements Violation {
        @Override
        public String toString() {
            return piece.getSimpleName() + " targets " + spec.getSimpleName()
                    + " which is not LoadBearing";
        }
    }

    /** A piece says done() but the spec does not say DONE, or vice-versa. */
    record StatusMismatch(Class<? extends Stateless> spec,
                          SpecStatus specStatus,
                          Class<? extends Stateless> piece,
                          boolean pieceDone) implements Violation {
        @Override
        public String toString() {
            return spec.getSimpleName() + " says " + specStatus
                    + " but " + piece.getSimpleName() + " says done=" + pieceDone;
        }
    }
}
