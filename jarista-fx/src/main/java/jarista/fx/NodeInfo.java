package jarista.fx;

import jarista.detail.RoledDetail;

import java.util.List;

/**
 * Display data for a single node in the feature graph tree.
 */
public record NodeInfo(
        String name,
        String fullName,
        int specLevel,          // 1–18 for specs, 0 for pieces
        boolean piece,
        List<String> dependencies,
        List<RoledDetail> details
) {

    public String levelTag() {
        if (piece) return "Piece";
        return "L" + specLevel;
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
