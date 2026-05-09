package jarista.tui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.*;

/**
 * Full-screen terminal UI for navigating a Jarista feature graph.
 *
 * <pre>
 *   Jarista Feature Graph                          30 nodes  0 violations
 *  ──────────────────────────────────────────────────────────────────────
 *   v * JaristaGoal                                            L1 Goal
 *   | v + CoreTypeSystem                                       L2 Epic
 *   | | |- o SpecSkeleton                                   L3 Feature
 *   | | |  '- &gt; ImplSpecSkeleton                               Piece
 *   ...
 *  ──────────────────────────────────────────────────────────────────────
 *   jarista.project.spec.CoreTypeSystem  deps: JaristaGoal
 *   Up/Down Navigate   Space Expand/Collapse   q Quit
 * </pre>
 */
public final class GraphTui {

    // ── flat row produced by tree traversal ───────────────────────

    record FlatRow(NodeView node, int depth, String prefix, boolean expandable) {}

    // ── state ────────────────────────────────────────────────────

    private final GraphModel model;
    private final Set<NodeView> expanded = new HashSet<>();
    private int selectedIndex;
    private int scrollOffset;

    public GraphTui(GraphModel model) {
        this.model = model;
        expandAll(model.root());
    }

    // ── main loop ────────────────────────────────────────────────

    public void run() throws IOException {
        Terminal terminal;
        try {
            // Prefer native console terminal (works in cmd, PowerShell, proper TTYs)
            terminal = new DefaultTerminalFactory()
                    .setPreferTerminalEmulator(false)
                    .createTerminal();
        } catch (IOException e) {
            // Console unavailable (exec:java, IDE run, no TTY) — Swing fallback
            terminal = new DefaultTerminalFactory()
                    .setInitialTerminalSize(new TerminalSize(110, 38))
                    .setPreferTerminalEmulator(true)
                    .createTerminal();
        }
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);

        try {
            boolean running = true;
            while (running) {
                List<FlatRow> rows = flattenTree();
                render(screen, rows);
                KeyStroke key = screen.readInput();
                running = handleInput(key, rows);
            }
        } finally {
            screen.stopScreen();
        }
    }

    // ── tree flattening ──────────────────────────────────────────

    private List<FlatRow> flattenTree() {
        List<FlatRow> rows = new ArrayList<>();
        if (model.root() != null) {
            flattenNode(model.root(), 0, "", true, rows);
        }
        return rows;
    }

    private void flattenNode(NodeView node, int depth, String inherited,
                              boolean isLast, List<FlatRow> rows) {
        String connector, childPrefix;
        if (depth == 0) {
            connector   = "";
            childPrefix = "";
        } else {
            connector   = isLast ? "'- " : "|- ";
            childPrefix = inherited + (isLast ? "   " : "|  ");
        }

        rows.add(new FlatRow(node, depth, inherited + connector,
                             !node.children().isEmpty()));

        if (expanded.contains(node)) {
            List<NodeView> ch = node.children();
            for (int i = 0; i < ch.size(); i++) {
                flattenNode(ch.get(i), depth + 1, childPrefix,
                           i == ch.size() - 1, rows);
            }
        }
    }

    // ── rendering ────────────────────────────────────────────────

    private void render(Screen screen, List<FlatRow> rows) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();
        TerminalSize size = screen.getTerminalSize();
        int w = size.getColumns();
        int h = size.getRows();

        // ─── header ──────────────────────────────────────────────
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.enableModifiers(SGR.BOLD);
        tg.putString(1, 0, "Jarista Feature Graph");
        tg.disableModifiers(SGR.BOLD);

        String stats = model.totalNodes() + " nodes  " +
                       model.violations().size() + " violations";
        tg.setForegroundColor(model.violations().isEmpty()
                ? TextColor.ANSI.GREEN : TextColor.ANSI.RED);
        tg.putString(Math.max(0, w - stats.length() - 1), 0, stats);

        // separator
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(0, 1, repeat('-', w));

        // ─── tree area ───────────────────────────────────────────
        int treeStart  = 2;
        int treeHeight = Math.max(1, h - 5);

        // scroll
        if (selectedIndex >= scrollOffset + treeHeight)
            scrollOffset = selectedIndex - treeHeight + 1;
        if (selectedIndex < scrollOffset)
            scrollOffset = selectedIndex;

        for (int i = 0; i < treeHeight && i + scrollOffset < rows.size(); i++) {
            renderRow(tg, rows.get(i + scrollOffset),
                      treeStart + i, w, (i + scrollOffset) == selectedIndex);
        }

        // ─── footer ─────────────────────────────────────────────
        int footerY = h - 3;
        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(0, footerY, repeat('-', w));

        // detail line
        if (selectedIndex >= 0 && selectedIndex < rows.size()) {
            FlatRow sel = rows.get(selectedIndex);
            StringBuilder detail = new StringBuilder("  ");
            detail.append(sel.node().fullName());
            if (!sel.node().dependencyNames().isEmpty()) {
                detail.append("  deps: ")
                      .append(String.join(", ", sel.node().dependencyNames()));
            }
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(0, footerY + 1, truncate(detail.toString(), w));
        }

        // key hints
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(1, footerY + 2,
                "Up/Down Navigate   Space Expand/Collapse   q Quit");

        screen.refresh();
    }

    private void renderRow(TextGraphics tg, FlatRow row, int y,
                           int width, boolean selected) {
        NodeView node = row.node();

        // background
        TextColor bg = selected ? TextColor.ANSI.BLUE : TextColor.ANSI.DEFAULT;
        tg.setBackgroundColor(bg);
        if (selected) {
            tg.setForegroundColor(bg);            // fill row with background
            tg.putString(0, y, repeat(' ', width));
        }

        int col = 1; // left margin

        // tree connectors
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(col, y, row.prefix());
        col += row.prefix().length();

        // expand / collapse indicator
        if (row.expandable()) {
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(col, y, expanded.contains(node) ? "v " : "> ");
            col += 2;
        }

        // icon
        String icon;
        TextColor iconColor;
        if (node.isPiece()) {
            icon = "> ";
            iconColor = TextColor.ANSI.YELLOW;
        } else {
            icon = switch (node.specLevel()) {
                case 1  -> "* ";
                case 2  -> "+ ";
                default -> "o ";
            };
            iconColor = colorForLevel(node);
        }
        tg.setForegroundColor(iconColor);
        tg.putString(col, y, icon);
        col += icon.length();

        // name
        tg.setForegroundColor(colorForLevel(node));
        if (!node.isPiece() && node.specLevel() <= 2) tg.enableModifiers(SGR.BOLD);
        tg.putString(col, y, node.name());
        tg.disableModifiers(SGR.BOLD);

        // right-aligned level tag
        String tag = node.levelTag();
        int tagX = width - tag.length() - 2;
        if (tagX > col + node.name().length() + 2) {
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(tagX, y, tag);
        }

        // reset
        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
    }

    // ── input ────────────────────────────────────────────────────

    private boolean handleInput(KeyStroke key, List<FlatRow> rows) {
        if (key.getKeyType() == KeyType.Escape) return false;
        if (key.getKeyType() == KeyType.Character) {
            char c = key.getCharacter();
            if (c == 'q' || c == 'Q') return false;
        }

        switch (key.getKeyType()) {
            case ArrowDown -> { if (selectedIndex < rows.size() - 1) selectedIndex++; }
            case ArrowUp   -> { if (selectedIndex > 0) selectedIndex--; }
            case Home      -> selectedIndex = 0;
            case End       -> selectedIndex = rows.size() - 1;
            case PageDown  -> selectedIndex = Math.min(rows.size() - 1, selectedIndex + 15);
            case PageUp    -> selectedIndex = Math.max(0, selectedIndex - 15);
            default -> {}
        }

        if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' '
                || key.getKeyType() == KeyType.Enter) {
            toggleExpand(rows);
        }

        return true;
    }

    private void toggleExpand(List<FlatRow> rows) {
        if (selectedIndex < 0 || selectedIndex >= rows.size()) return;
        FlatRow row = rows.get(selectedIndex);
        if (!row.expandable()) return;
        if (expanded.contains(row.node())) {
            expanded.remove(row.node());
        } else {
            expanded.add(row.node());
        }
    }

    // ── helpers ──────────────────────────────────────────────────

    private void expandAll(NodeView node) {
        if (node == null) return;
        if (!node.children().isEmpty()) expanded.add(node);
        node.children().forEach(this::expandAll);
    }

    private static TextColor colorForLevel(NodeView node) {
        if (node.isPiece()) return TextColor.ANSI.YELLOW;
        return switch (node.specLevel()) {
            case 1  -> TextColor.ANSI.WHITE;
            case 2  -> TextColor.ANSI.CYAN;
            default -> TextColor.ANSI.GREEN;
        };
    }

    private static String repeat(char ch, int n) {
        char[] buf = new char[n];
        Arrays.fill(buf, ch);
        return new String(buf);
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "~";
    }
}
