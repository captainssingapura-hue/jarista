package jarista.graph;

import jarista.Stateless;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class InstanceEnforcer {

    public List<Violation> check(List<Class<? extends Stateless>> classes) {
        var violations = new ArrayList<Violation>();
        for (var cls : classes) {
            if (!hasStaticInstance(cls)) {
                violations.add(new Violation.MissingInstance(cls));
            }
        }
        return violations;
    }

    private boolean hasStaticInstance(Class<?> cls) {
        try {
            Field f = cls.getField("INSTANCE");
            int mods = f.getModifiers();
            return Modifier.isPublic(mods)
                    && Modifier.isStatic(mods)
                    && Modifier.isFinal(mods)
                    && cls.isAssignableFrom(f.getType());
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
