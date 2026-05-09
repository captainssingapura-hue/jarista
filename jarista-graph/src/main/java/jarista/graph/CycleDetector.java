package jarista.graph;

import jarista.Stateless;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class CycleDetector {

    public List<Violation> check(List<Class<? extends Stateless>> classes) {
        Map<Class<?>, Set<Class<?>>> adjacency = buildAdjacency(classes);
        return detectCycles(adjacency);
    }

    private Map<Class<?>, Set<Class<?>>> buildAdjacency(List<Class<? extends Stateless>> classes) {
        var adj = new HashMap<Class<?>, Set<Class<?>>>();
        for (var cls : classes) {
            adj.putIfAbsent(cls, new HashSet<>());
            try {
                Object instance = cls.getField("INSTANCE").get(null);
                Method deps = cls.getMethod("dependencies");
                @SuppressWarnings("unchecked")
                Set<Stateless> neighbors = (Set<Stateless>) deps.invoke(instance);
                for (Stateless n : neighbors) {
                    adj.get(cls).add(n.getClass());
                }
            } catch (NoSuchFieldException | NoSuchMethodException e) {
                // no INSTANCE or no dependencies() — skip
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to read dependencies from " + cls.getSimpleName(), e);
            }
        }
        return adj;
    }

    private List<Violation> detectCycles(Map<Class<?>, Set<Class<?>>> adj) {
        var violations = new ArrayList<Violation>();
        var visited = new HashSet<Class<?>>();
        var onStack = new LinkedHashSet<Class<?>>();

        for (var node : adj.keySet()) {
            if (!visited.contains(node)) {
                dfs(node, adj, visited, onStack, violations);
            }
        }
        return violations;
    }

    private void dfs(Class<?> node, Map<Class<?>, Set<Class<?>>> adj,
                     Set<Class<?>> visited, LinkedHashSet<Class<?>> onStack,
                     List<Violation> violations) {
        visited.add(node);
        onStack.add(node);

        for (var neighbor : adj.getOrDefault(node, Set.of())) {
            if (onStack.contains(neighbor)) {
                violations.add(new Violation.CyclicDependency(formatCycle(onStack, neighbor)));
            } else if (!visited.contains(neighbor)) {
                dfs(neighbor, adj, visited, onStack, violations);
            }
        }

        onStack.remove(node);
    }

    private String formatCycle(LinkedHashSet<Class<?>> onStack, Class<?> back) {
        var sb = new StringBuilder();
        boolean inCycle = false;
        for (var cls : onStack) {
            if (cls == back) inCycle = true;
            if (inCycle) {
                if (!sb.isEmpty()) sb.append(" → ");
                sb.append(cls.getSimpleName());
            }
        }
        sb.append(" → ").append(back.getSimpleName());
        return sb.toString();
    }
}
