package jarista.web;

import hue.captains.singapura.js.homing.studio.base.Bootstrap;
import hue.captains.singapura.js.homing.studio.base.DefaultRuntimeParams;
import hue.captains.singapura.js.homing.studio.base.Umbrella;

/**
 * Jarista web workspace server. Bootstraps a single-studio umbrella
 * ({@link JaristaStudio}) with {@link JaristaFixtures} and starts the Vert.x
 * server on port 8083.
 *
 * <p>Run with {@code mvn -pl jarista-web exec:java}. Then open
 * {@code http://localhost:8083} and choose "Jarista Feature Graph".
 */
public final class JaristaStudioServer {

    private JaristaStudioServer() {}

    public static void main(String[] args) {
        var umbrella = new Umbrella.Solo<>(JaristaStudio.INSTANCE);
        new Bootstrap<>(new JaristaFixtures<>(umbrella), new DefaultRuntimeParams(8083)).start();
    }
}
