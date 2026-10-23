package jarista.web;

import hue.captains.singapura.js.homing.core.AppModule;
import hue.captains.singapura.js.homing.studio.base.Fixtures;
import hue.captains.singapura.js.homing.studio.base.Studio;
import hue.captains.singapura.js.homing.studio.base.Umbrella;
import hue.captains.singapura.js.homing.studio.starter.StudioStarterFixtures;
import hue.captains.singapura.tao.http.action.GetAction;
import hue.captains.singapura.tao.ontology.ValueObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Jarista's {@link Fixtures}: {@link StudioStarterFixtures} — the
 * batteries-included studio + workspace wiring (the Studio Workspace,
 * {@code /catalogue-tree}, the doc viewers) — with nothing added. The feature
 * graph reaches the studio through {@link JaristaHome}'s leaves and
 * sub-catalogues, not through fixtures.
 */
public record JaristaFixtures<S extends Studio<?>>(Umbrella<S> umbrella)
        implements Fixtures<S>, ValueObject {

    public JaristaFixtures {
        Objects.requireNonNull(umbrella);
    }

    private StudioStarterFixtures<S> starter() {
        return new StudioStarterFixtures<>(umbrella);
    }

    @Override public List<AppModule<?, ?>> harnessApps() {
        return starter().harnessApps();
    }

    @Override public NodeChrome chromeFor(Umbrella<S> node) {
        return starter().chromeFor(node);
    }

    @Override public Map<String, GetAction<RoutingContext, ?, ?, ?>> harnessGetActions() {
        return starter().harnessGetActions();
    }
}
