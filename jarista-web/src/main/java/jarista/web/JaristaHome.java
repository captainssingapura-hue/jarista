package jarista.web;

import hue.captains.singapura.js.homing.studio.base.Doc;
import hue.captains.singapura.js.homing.studio.base.DocProvider;
import hue.captains.singapura.js.homing.studio.base.app.DocTreeViewer;
import hue.captains.singapura.js.homing.studio.base.app.Entry;
import hue.captains.singapura.js.homing.studio.base.app.L0_Catalogue;
import hue.captains.singapura.js.homing.studio.base.composed.ComposedViewer;

import java.util.List;

/**
 * Home catalogue for the Jarista studio. A stateless record — the shared
 * {@code CatalogueAppHost} renders it. Implements {@link DocProvider} so the
 * docs are reachable through the studio's {@code DocRegistry} (required for
 * reachability validation at boot).
 *
 * <p>Two home tiles:
 * <ul>
 *   <li>the intro doc (a ComposedDoc, viewed by {@link ComposedViewer});</li>
 *   <li>the whole feature graph as a foldable {@link JaristaRigidTree} doc
 *       (viewed by {@link DocTreeViewer}).</li>
 * </ul>
 * In Homing 0.8.x a doc entry names its viewer app and params explicitly.
 *
 * <p>The graph is deliberately <em>not</em> mirrored into nested sub-catalogues:
 * 0.8.x keys a catalogue's navigation identity by its class and forbids one
 * identity at many positions (RFC 0053, Law 1), so a data-driven,
 * many-instances-per-class catalogue cannot boot. The foldable RigidDoc is the
 * framework's shape for "bring your own tree".
 */
public record JaristaHome() implements L0_Catalogue<JaristaHome>, DocProvider {

    public static final JaristaHome INSTANCE = new JaristaHome();

    @Override public String name() { return "Jarista"; }

    @Override public String summary() {
        return "Graph-native, repo-resident project management for Java — the feature graph, browsable.";
    }

    @Override public List<Entry<JaristaHome>> leaves() {
        return List.of(
                Entry.of(this, ComposedViewer.INSTANCE,
                        new ComposedViewer.Params(JaristaIntroDoc.INSTANCE.uuid().toString()),
                        JaristaIntroDoc.INSTANCE),
                Entry.of(this, DocTreeViewer.INSTANCE,
                        new DocTreeViewer.Params(JaristaRigidTree.id()),
                        JaristaRigidTree.INSTANCE)
        );
    }

    @Override public List<Doc> docs() {
        return List.of(JaristaIntroDoc.INSTANCE, JaristaRigidTree.INSTANCE);
    }
}
