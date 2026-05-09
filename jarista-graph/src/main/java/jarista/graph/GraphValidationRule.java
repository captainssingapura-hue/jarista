package jarista.graph;

import java.util.List;

public abstract class GraphValidationRule {

    protected abstract String[] packages();

    public void validate() {
        GraphValidator validator = new GraphValidator();
        List<Violation> violations = validator.validate(packages());
        if (!violations.isEmpty()) {
            var sb = new StringBuilder("Graph validation failed with ")
                    .append(violations.size())
                    .append(" violation(s):\n");
            for (Violation v : violations) {
                sb.append("  - ").append(v).append('\n');
            }
            throw new AssertionError(sb.toString());
        }
    }
}
