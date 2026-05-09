package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Barista Brew cartoon: raw beans become a structured graph.
 */
public class CartoonBaristaBrew implements L4_Spec<BaristaIdentity> {
    public static final CartoonBaristaBrew INSTANCE = new CartoonBaristaBrew();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("A barista turns raw beans into something people actually want.")
                .ln("Jarista turns raw Java classes into a validated project graph.")
            .as(DESCRIPTION),

            _db.html()
                .ln("<svg viewBox='0 0 480 200' xmlns='http://www.w3.org/2000/svg'>")
                .ln("  <rect x='0' y='0' width='480' height='200' rx='12' fill='#1a1a2e'/>")
                // Step 1: Beans (raw classes)
                .ln("  <g transform='translate(60,80)'>")
                .ln("    <ellipse cx='0' cy='0' rx='35' ry='28' fill='#3d2d20'/>")
                .ln("    <text x='0' y='-2' text-anchor='middle' font-size='20'>☕</text>")
                .ln("    <text x='0' y='18' text-anchor='middle' fill='#a6adc8' font-size='8' font-family='sans-serif'>raw .java</text>")
                .ln("    <text x='0' y='45' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>Beans</text>")
                .ln("  </g>")
                // Arrow 1
                .ln("  <line x1='100' y1='80' x2='140' y2='80' stroke='#8b7355' stroke-width='2'/>")
                .ln("  <polygon points='140,75 150,80 140,85' fill='#8b7355'/>")
                // Step 2: Grind (scan + classify)
                .ln("  <g transform='translate(190,80)'>")
                .ln("    <rect x='-30' y='-25' width='60' height='50' rx='8' fill='#453d28'/>")
                .ln("    <text x='0' y='-2' text-anchor='middle' font-size='18'>⚙️</text>")
                .ln("    <text x='0' y='16' text-anchor='middle' fill='#f9e2af' font-size='8' font-family='sans-serif'>scan</text>")
                .ln("    <text x='0' y='45' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>Grind</text>")
                .ln("  </g>")
                // Arrow 2
                .ln("  <line x1='225' y1='80' x2='265' y2='80' stroke='#8b7355' stroke-width='2'/>")
                .ln("  <polygon points='265,75 275,80 265,85' fill='#8b7355'/>")
                // Step 3: Brew (validate)
                .ln("  <g transform='translate(315,80)'>")
                .ln("    <rect x='-30' y='-25' width='60' height='50' rx='8' fill='#28452e'/>")
                .ln("    <text x='0' y='-2' text-anchor='middle' font-size='18'>🔥</text>")
                .ln("    <text x='0' y='16' text-anchor='middle' fill='#a6e3a1' font-size='8' font-family='sans-serif'>validate</text>")
                .ln("    <text x='0' y='45' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>Brew</text>")
                .ln("  </g>")
                // Arrow 3
                .ln("  <line x1='350' y1='80' x2='390' y2='80' stroke='#8b7355' stroke-width='2'/>")
                .ln("  <polygon points='390,75 400,80 390,85' fill='#8b7355'/>")
                // Step 4: Serve (graph)
                .ln("  <g transform='translate(430,80)'>")
                .ln("    <circle cx='0' cy='-8' r='12' fill='#d4b896'/>")
                .ln("    <circle cx='-14' cy='12' r='9' fill='#d4b896'/>")
                .ln("    <circle cx='14' cy='12' r='9' fill='#d4b896'/>")
                .ln("    <line x1='0' y1='4' x2='-14' y2='3' stroke='#8b7355' stroke-width='1.5'/>")
                .ln("    <line x1='0' y1='4' x2='14' y2='3' stroke='#8b7355' stroke-width='1.5'/>")
                .ln("    <text x='0' y='45' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>Serve</text>")
                .ln("  </g>")
                // Title
                .ln("  <text x='240' y='175' text-anchor='middle' fill='#a6adc8' font-size='11' font-family='sans-serif'>Beans → Grind → Brew → Serve: from raw classes to validated graph</text>")
                .ln("</svg>")
            .as(PRODUCT)
        );
    }
}
