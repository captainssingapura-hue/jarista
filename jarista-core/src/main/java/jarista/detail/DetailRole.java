package jarista.detail;

/**
 * Semantic role of a detail block attached to a spec or piece.
 * The UI layer may use these to group, order, or style content.
 */
public enum DetailRole {

    /** What this node is — its purpose and scope. */
    DESCRIPTION,

    /** What "done" looks like — acceptance criteria, observable outcomes. */
    EXPECTATION,

    /** Why this node exists — motivation, the problem it solves. */
    RATIONALE,

    /** How it works — design notes, algorithms, key decisions. */
    DESIGN,

    /** Usage examples — code samples, CLI invocations, screenshots. */
    EXAMPLE,

    /** Links to external material — docs, specs, diagrams, papers. */
    REFERENCE,

    /** A deliverable artifact — HTML page, diagram, cartoon, report. */
    PRODUCT,

    /** Anything that doesn't fit the other roles. */
    NOTE
}
