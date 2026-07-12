package jarista.spec;

/**
 * Marker for leaf specs that represent concrete deliverables.
 *
 * <p>A load-bearing spec must explicitly declare its status.
 * Only load-bearing specs may have pieces attached.
 * Organizational (non-leaf) specs derive their status from
 * descendant leaves — indicative only, since software is a
 * living organism with perpetual room for improvement.
 *
 * <p>No default — if you declare yourself load-bearing, you
 * must say where you stand.
 */
public interface LoadBearing {

    SpecStatus status();
}
