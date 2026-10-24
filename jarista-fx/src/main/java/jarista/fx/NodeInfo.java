package jarista.fx;

import jarista.detail.RoledDetail;
import jarista.spec.SpecStatus;

import java.util.List;

/**
 * Display data for a single node in the feature graph tree.
 */
public record NodeInfo(
        String name,
        String fullName,
        int specLevel,          // 0–18 for specs (0 = vision root), 0 for pieces (distinguished by `piece`)
        boolean piece,
        boolean loadBearing,    // true for leaf specs implementing LoadBearing
        SpecStatus status,      // declared status for load-bearing, derived for organizational, null for pieces
        List<String> dependencies,
        List<RoledDetail> details,
        Boolean done,           // pieces: done(); null for specs
        List<String> outOfScope,        // pieces: what not to touch; empty otherwise
        String verificationTarget       // pieces: test/command that proves it; "" if none
) {

    public String levelTag() {
        if (piece) return "Piece";
        return "L" + specLevel;
    }

    /** ✓ for a done piece, ○ for an open one, "" for specs. */
    public String doneMark() {
        if (done == null) return "";
        return done ? "✓" : "○";
    }

    public String icon() {
        if (piece) return "▸";   // ▸
        return switch (specLevel) {
            case 1  -> "◉";      // ◉
            case 2  -> "◆";      // ◆
            default -> "○";      // ○
        };
    }
}
