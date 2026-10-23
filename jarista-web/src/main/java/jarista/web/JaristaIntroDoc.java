package jarista.web;

import hue.captains.singapura.js.homing.studio.base.composed.ComposedDoc;
import hue.captains.singapura.js.homing.studio.base.composed.MarkdownSegment;
import hue.captains.singapura.js.homing.studio.base.composed.Segment;

import java.util.List;
import java.util.Optional;

/**
 * Landing page for the Jarista studio. Holder for the {@link ComposedDoc}
 * {@code INSTANCE} (the class itself is not a Doc), following the reference's
 * ComposedDoc convention.
 */
public final class JaristaIntroDoc {

    private JaristaIntroDoc() {}

    public static final ComposedDoc INSTANCE = build();

    private static ComposedDoc build() {
        var welcome = new MarkdownSegment(
                """
                # Jarista

                Graph-native, repo-resident project management for Java.
                No YAML, no dashboards, no accountability theater — the graph lives in
                compiled code, and if it compiles, the structure is valid.

                Open **Jarista Feature Graph** from the home tiles to browse the
                self-hosted spec/piece graph as a tree. Click any node to read its
                typed details — description, rationale, design notes, and the
                cartoons — rendered inline.

                > 结图为谱，南郭立现 — weave the graph into a score, and the pretender is revealed.
                """,
                Optional.of("Welcome"));

        return ComposedDoc.of(
                ComposedDoc.deterministicUuid("jarista:intro"),
                "Jarista",
                "Graph-native project management for Java — browse the self-hosted feature graph.",
                "HOME",
                List.<Segment>of(welcome));
    }
}
