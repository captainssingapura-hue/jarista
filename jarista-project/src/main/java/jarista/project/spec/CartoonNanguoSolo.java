package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — The Nanguo Solo cartoon: Mr. Nanguo exposed when the ensemble becomes a solo.
 */
public class CartoonNanguoSolo implements L4_Spec<NanguoAnalogy> {
    public static final CartoonNanguoSolo INSTANCE = new CartoonNanguoSolo();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("南郭先生 hid in the 300-man orchestra. When the king demanded solos,")
                .ln("he fled. This cartoon illustrates the moment of exposure.")
            .as(DESCRIPTION),

            _db.html()
                .ln("<svg viewBox='0 0 480 280' xmlns='http://www.w3.org/2000/svg'>")
                // Background stage
                .ln("  <rect x='0' y='0' width='480' height='280' rx='12' fill='#1a1a2e'/>")
                .ln("  <rect x='20' y='200' width='440' height='60' rx='8' fill='#2d2040'/>")
                .ln("  <text x='240' y='240' text-anchor='middle' fill='#6c7086' font-size='11' font-family='sans-serif'>🎵 Stage 🎵</text>")
                // Spotlight
                .ln("  <ellipse cx='360' cy='195' rx='55' ry='8' fill='#f9e2af' opacity='0.15'/>")
                .ln("  <polygon points='310,0 410,0 415,195 305,195' fill='#f9e2af' opacity='0.06'/>")
                // Orchestra group (left) — happy players
                .ln("  <g transform='translate(60,120)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='16'>🎵</text>")
                .ln("    <text x='0' y='30' text-anchor='middle' fill='#a6e3a1' font-size='10' font-family='sans-serif'>Playing</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(120,110)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='16'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(180,125)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='16'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(100,160)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='16'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(160,165)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='16'>🎵</text>")
                .ln("  </g>")
                // Nanguo — fleeing from spotlight
                .ln("  <g transform='translate(360,130)'>")
                .ln("    <circle cx='0' cy='0' r='22' fill='#f38ba8' stroke='#f9e2af' stroke-width='2'/>")
                .ln("    <text x='0' y='6' text-anchor='middle' font-size='18'>😰</text>")
                .ln("    <text x='0' y='35' text-anchor='middle' fill='#f38ba8' font-size='11' font-weight='bold' font-family='sans-serif'>南郭</text>")
                .ln("    <text x='0' y='50' text-anchor='middle' fill='#f38ba8' font-size='9' font-family='sans-serif'>No sound!</text>")
                .ln("  </g>")
                // Flee arrow
                .ln("  <path d='M385,125 Q430,90 450,60' stroke='#f38ba8' stroke-width='2' fill='none' stroke-dasharray='5,4'/>")
                .ln("  <polygon points='450,55 455,68 443,65' fill='#f38ba8'/>")
                .ln("  <text x='445' y='50' fill='#f38ba8' font-size='10' font-family='sans-serif'>flee!</text>")
                // King demanding solo
                .ln("  <g transform='translate(240,45)'>")
                .ln("    <rect x='-60' y='-20' width='120' height='35' rx='6' fill='#45475a'/>")
                .ln("    <text x='0' y='2' text-anchor='middle' fill='#f9e2af' font-size='12' font-family='sans-serif'>👑 King Min: \"Solo!\"</text>")
                .ln("  </g>")
                // Label
                .ln("  <text x='240' y='272' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>The ensemble hid the faker. The solo exposed him.</text>")
                .ln("</svg>")
            .as(PRODUCT)
        );
    }
}
