package jarista.graph;

import jarista.Stateless;

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
}
