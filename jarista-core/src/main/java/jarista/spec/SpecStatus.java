package jarista.spec;

/**
 * Declared status of a load-bearing (leaf) spec.
 *
 * <p>{@link #PLANNED}, {@link #IN_PROGRESS} and {@link #DONE} are the active
 * lifecycle. {@link #PARKED} and {@link #ABANDONED} are the honest alternatives
 * to leaving a spec PLANNED forever: a parked spec is still wanted but
 * deliberately not being worked on; an abandoned spec is no longer wanted and
 * stays in the graph only as a record of the decision.
 *
 * <p>Neither inactive state counts toward the derived status of an
 * organizational parent — see {@link #active()}.
 */
public enum SpecStatus {
    PLANNED,
    IN_PROGRESS,
    DONE,
    /** Deliberately shelved; still intended, not being worked on. */
    PARKED,
    /** Deliberately dropped; kept in the graph as a record, not a plan. */
    ABANDONED;

    /** True for statuses that represent work still in the plan (PLANNED, IN_PROGRESS, DONE). */
    public boolean active() {
        return this != PARKED && this != ABANDONED;
    }
}
