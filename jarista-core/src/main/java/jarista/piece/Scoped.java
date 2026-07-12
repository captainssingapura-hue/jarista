package jarista.piece;

import java.util.List;

/**
 * Optional interface for pieces that need explicit scope boundaries.
 *
 * <p>Critical for agentic development — an AI agent reads {@link #outOfScope()}
 * and constrains itself. This is not bureaucracy; it is a guardrail
 * that makes autonomy safe.
 */
public interface Scoped {

    /**
     * What this piece should NOT touch.
     * Free-form strings: file paths, module names, API boundaries, constraints.
     *
     * @return scope exclusions, empty if unconstrained
     */
    default List<String> outOfScope() {
        return List.of();
    }
}
