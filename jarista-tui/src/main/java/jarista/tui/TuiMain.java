package jarista.tui;

import java.io.IOException;

/**
 * CLI entry point for the Jarista TUI.
 *
 * <p>Usage: {@code java jarista.tui.TuiMain <package> [<package>...]}
 *
 * <p>Example:
 * <pre>
 *   mvn -pl jarista-tui exec:java -Dexec.args="jarista.project"
 * </pre>
 */
public final class TuiMain {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: jarista-tui <package> [<package>...]");
            System.err.println("  Scans the given packages for Stateless nodes");
            System.err.println("  and displays the feature graph in your terminal.");
            System.exit(1);
        }

        var model = new GraphModel(args);
        model.scan();

        if (model.root() == null) {
            System.err.println("No L1 root spec found in packages: "
                               + String.join(", ", args));
            System.exit(2);
        }

        System.out.println("Scanned " + model.totalNodes() + " nodes, "
                           + model.violations().size() + " violations.");
        System.out.println("Launching TUI...");

        new GraphTui(model).run();
    }
}
