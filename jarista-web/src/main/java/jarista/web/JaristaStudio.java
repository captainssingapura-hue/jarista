package jarista.web;

import hue.captains.singapura.js.homing.studio.base.Studio;
import hue.captains.singapura.js.homing.studio.base.app.StudioBrand;

/**
 * The Jarista studio's typed bundle. Home is {@link JaristaHome}; brand
 * "Jarista". No {@code apps()} of its own — the tree app arrives from the
 * {@code trees()} seam, and the Studio Workspace ("studio" spec) from the
 * starter.
 */
public record JaristaStudio() implements Studio<JaristaHome> {

    public static final JaristaStudio INSTANCE = new JaristaStudio();

    @Override public JaristaHome home() { return JaristaHome.INSTANCE; }

    @Override public StudioBrand standaloneBrand() {
        return new StudioBrand("Jarista", JaristaHome.class);
    }
}
