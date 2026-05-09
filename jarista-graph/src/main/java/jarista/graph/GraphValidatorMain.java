package jarista.graph;

import java.util.List;

public final class GraphValidatorMain {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: GraphValidatorMain <package> [<package>...]");
            System.exit(1);
        }

        var validator = new GraphValidator();
        List<Violation> violations = validator.validate(args);

        if (violations.isEmpty()) {
            System.out.println("Graph validation passed — no violations found.");
        } else {
            System.err.println("Graph validation FAILED with " + violations.size() + " violation(s):");
            for (Violation v : violations) {
                System.err.println("  - " + v);
            }
            System.exit(1);
        }
    }
}
