package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L4_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/**
 * L4 — 虽猫可代之: even a cat can replace the faker once the structure is clear.
 */
public class CartoonTamaCat implements L4_Spec<NanguoAnalogy> {
    public static final CartoonTamaCat INSTANCE = new CartoonTamaCat();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("虽猫可代之 — even a cat can replace them.")
                .ln("Once the 南郭s flee and the structure is clear,")
                .ln("the work itself is not mysterious. A tama cat,")
                .ln("following the graph, can do what the faker pretended to.")
            .as(DESCRIPTION),

            _db.text()
                .ln("This is not an insult to the work. It is a compliment")
                .ln("to the structure. When a project is well-decomposed,")
                .ln("each piece is small enough that competence — not heroism —")
                .ln("is sufficient. The cat is the proof that the graph works.")
            .as(RATIONALE),

            _db.html()
                .ln("<svg viewBox='0 0 480 260' xmlns='http://www.w3.org/2000/svg'>")
                .ln("  <rect x='0' y='0' width='480' height='260' rx='12' fill='#1a1a2e'/>")
                // Scene divider
                .ln("  <line x1='240' y1='20' x2='240' y2='230' stroke='#313244' stroke-width='1' stroke-dasharray='4,4'/>")
                .ln("  <text x='120' y='30' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>Before: 南郭 hides</text>")
                .ln("  <text x='360' y='30' text-anchor='middle' fill='#6c7086' font-size='10' font-family='sans-serif'>After: 🐱 replaces</text>")
                // Left scene — Nanguo hiding in crowd
                .ln("  <g transform='translate(70,100)'>")
                .ln("    <circle cx='0' cy='0' r='16' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='14'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(120,90)'>")
                .ln("    <circle cx='0' cy='0' r='16' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='14'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(170,105)'>")
                .ln("    <circle cx='0' cy='0' r='18' fill='#f38ba8' opacity='0.7'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='14'>🤫</text>")
                .ln("    <text x='0' y='28' text-anchor='middle' fill='#f38ba8' font-size='9' font-family='sans-serif'>南郭</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(100,145)'>")
                .ln("    <circle cx='0' cy='0' r='16' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='14'>🎵</text>")
                .ln("  </g>")
                .ln("  <g transform='translate(155,150)'>")
                .ln("    <circle cx='0' cy='0' r='16' fill='#89b4fa'/>")
                .ln("    <text x='0' y='5' text-anchor='middle' font-size='14'>🎵</text>")
                .ln("  </g>")
                .ln("  <text x='120' y='200' text-anchor='middle' fill='#f38ba8' font-size='11' font-family='sans-serif'>Who is actually playing?</text>")
                // Right scene — cat with clear graph
                .ln("  <g transform='translate(360,80)'>")
                // Mini graph
                .ln("    <circle cx='0' cy='-20' r='8' fill='#d4b896'/>")
                .ln("    <circle cx='-20' cy='5' r='6' fill='#d4b896'/>")
                .ln("    <circle cx='20' cy='5' r='6' fill='#d4b896'/>")
                .ln("    <line x1='0' y1='-12' x2='-20' y2='-1' stroke='#8b7355' stroke-width='1.5'/>")
                .ln("    <line x1='0' y1='-12' x2='20' y2='-1' stroke='#8b7355' stroke-width='1.5'/>")
                .ln("  </g>")
                // Cat
                .ln("  <g transform='translate(360,140)'>")
                .ln("    <text x='0' y='0' text-anchor='middle' font-size='36'>🐱</text>")
                .ln("    <text x='0' y='30' text-anchor='middle' fill='#a6e3a1' font-size='10' font-weight='bold' font-family='sans-serif'>tama</text>")
                .ln("  </g>")
                .ln("  <text x='360' y='200' text-anchor='middle' fill='#a6e3a1' font-size='11' font-family='sans-serif'>Clear graph. Cat can do it.</text>")
                // Bottom label
                .ln("  <text x='240' y='245' text-anchor='middle' fill='#f9e2af' font-size='11' font-family='sans-serif'>虽猫可代之 — even a cat can replace them</text>")
                .ln("</svg>")
            .as(PRODUCT)
        );
    }
}
