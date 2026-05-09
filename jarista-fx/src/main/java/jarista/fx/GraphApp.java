package jarista.fx;

import jarista.detail.Detail;
import jarista.detail.DetailRole;
import jarista.detail.RoledDetail;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JavaFX desktop viewer for Jarista feature graphs.
 *
 * <p>Composition root — wires the TreeView, detail panel, toolbar and
 * status bar into a single scene.  No FXML; pure programmatic layout
 * following the notation-ui pattern.
 */
public class GraphApp extends Application {

    // ── Catppuccin Mocha palette ─────────────────────────────────
    static final String PINK   = "#f5c2e7";
    static final String BLUE   = "#89b4fa";
    static final String GREEN  = "#a6e3a1";
    static final String YELLOW = "#f9e2af";
    static final String RED    = "#f38ba8";
    static final String TEXT   = "#cdd6f4";
    static final String SUB    = "#a6adc8";
    static final String MUTED  = "#6c7086";

    private TreeView<NodeInfo> treeView;
    private VBox detailContent;
    private int totalNodes;
    private int violationCount;
    private int specCount;
    private int pieceCount;

    // ── lifecycle ────────────────────────────────────────────────

    @Override
    public void start(Stage stage) {
        String[] packages = getParameters().getRaw().toArray(String[]::new);
        if (packages.length == 0) packages = new String[]{"jarista.project"};

        var builder = new GraphTreeBuilder(packages);
        builder.build();
        totalNodes     = builder.totalNodes();
        violationCount = builder.violationCount();
        specCount      = builder.specCount();
        pieceCount     = builder.pieceCount();

        BorderPane root = new BorderPane();
        root.setTop(buildToolbar());
        root.setCenter(buildCenter(builder.rootItem()));
        root.setBottom(buildStatusBar(packages));

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                getClass().getResource("/graph-theme.css").toExternalForm());

        stage.setTitle("Jarista — Feature Graph");
        for (int sz : new int[]{256, 64, 32, 16}) {
            stage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/jarista-icon-" + sz + ".png")));
        }
        stage.setScene(scene);
        stage.show();

        treeView.getSelectionModel().selectFirst();
    }

    public static void main(String[] args) { launch(args); }

    // ── toolbar ──────────────────────────────────────────────────

    private HBox buildToolbar() {
        Label title = new Label("Jarista");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Feature Graph");
        subtitle.getStyleClass().add("app-subtitle");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label stats = new Label(totalNodes + " nodes · " +
                                violationCount + " violations");
        stats.getStyleClass().add(violationCount == 0 ? "stats-ok" : "stats-error");

        HBox bar = new HBox(10, title, subtitle, spacer, stats);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.getStyleClass().add("toolbar");
        return bar;
    }

    // ── center: tree + detail ────────────────────────────────────

    private SplitPane buildCenter(TreeItem<NodeInfo> rootItem) {
        // ── tree ──
        treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        treeView.getStyleClass().add("graph-tree");
        treeView.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(NodeInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                Label icon = new Label(item.icon());
                icon.setStyle("-fx-text-fill: " + colorFor(item) + ";"
                            + " -fx-font-size: 14;");
                icon.setMinWidth(18);

                Label name = new Label(item.name());
                name.setStyle("-fx-text-fill: " + TEXT + ";"
                        + (!item.piece() && item.specLevel() <= 2
                                ? " -fx-font-weight: bold;" : ""));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label tag = new Label(item.levelTag());
                tag.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 11;");

                HBox row = new HBox(6, icon, name, spacer, tag);
                row.setAlignment(Pos.CENTER_LEFT);
                row.prefWidthProperty().bind(
                        tv.widthProperty().subtract(getTreeItemLevel(getTreeItem()) * 18 + 30));

                setGraphic(row);
                setText(null);
            }
        });

        // ── detail panel ──
        detailContent = new VBox(10);
        detailContent.setPadding(new Insets(20));
        detailContent.getStyleClass().add("detail-panel");

        ScrollPane detailScroll = new ScrollPane(detailContent);
        detailScroll.setFitToWidth(true);
        detailScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        treeView.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> updateDetail(sel));

        SplitPane split = new SplitPane(treeView, detailScroll);
        split.setDividerPositions(0.55);
        return split;
    }

    private int getTreeItemLevel(TreeItem<?> item) {
        int level = 0;
        TreeItem<?> p = item;
        while (p != null) { p = p.getParent(); level++; }
        return level;
    }

    // ── detail panel ─────────────────────────────────────────────

    private void updateDetail(TreeItem<NodeInfo> sel) {
        detailContent.getChildren().clear();
        if (sel == null || sel.getValue() == null) return;

        NodeInfo info = sel.getValue();

        // name
        Label name = new Label(info.name());
        name.setStyle("-fx-font-size: 22; -fx-font-weight: bold;"
                    + " -fx-text-fill: " + colorFor(info) + ";");

        // full class path
        Label fqn = new Label(info.fullName());
        fqn.setStyle("-fx-font-family: monospace; -fx-font-size: 12;"
                   + " -fx-text-fill: " + SUB + ";");
        fqn.setWrapText(true);

        // type badge
        Label badge = new Label(info.levelTag());
        badge.getStyleClass().addAll("badge", badgeClass(info));

        detailContent.getChildren().addAll(name, fqn, badge, new Separator());

        // dependencies
        if (!info.dependencies().isEmpty()) {
            addSection("Dependencies", info.dependencies().stream()
                    .map(d -> "→ " + d).toList(), BLUE);
        }

        // child specs (from TreeItem children)
        List<TreeItem<NodeInfo>> childSpecs = sel.getChildren().stream()
                .filter(c -> !c.getValue().piece()).toList();
        if (!childSpecs.isEmpty()) {
            addSection("Child Specs (" + childSpecs.size() + ")",
                    childSpecs.stream().map(c -> "○ " + c.getValue().name()).toList(),
                    GREEN);
        }

        // pieces
        List<TreeItem<NodeInfo>> pieces = sel.getChildren().stream()
                .filter(c -> c.getValue().piece()).toList();
        if (!pieces.isEmpty()) {
            addSection("Pieces (" + pieces.size() + ")",
                    pieces.stream().map(c -> "▸ " + c.getValue().name()).toList(),
                    YELLOW);
        }

        // ── details by role ───────────────────────────────────────
        if (!info.details().isEmpty()) {
            detailContent.getChildren().add(new Separator());

            Map<DetailRole, List<RoledDetail>> byRole = info.details().stream()
                    .collect(Collectors.groupingBy(RoledDetail::role,
                            java.util.LinkedHashMap::new, Collectors.toList()));

            for (var entry : byRole.entrySet()) {
                Label roleHeader = new Label(formatRole(entry.getKey()));
                roleHeader.setStyle("-fx-font-size: 13; -fx-font-weight: bold;"
                        + " -fx-text-fill: " + roleColor(entry.getKey()) + ";"
                        + " -fx-padding: 8 0 4 0;");
                detailContent.getChildren().add(roleHeader);

                for (RoledDetail rd : entry.getValue()) {
                    detailContent.getChildren().add(renderDetail(rd.detail()));
                }
            }
        }

        // empty leaf message
        if (info.details().isEmpty() && info.dependencies().isEmpty()
                && sel.getChildren().isEmpty()) {
            Label leaf = new Label("Leaf node — no details, children, or dependencies");
            leaf.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-style: italic;");
            detailContent.getChildren().add(leaf);
        }
    }

    private javafx.scene.Node renderDetail(Detail detail) {
        return switch (detail) {
            case Detail.Text(var content) -> {
                Label l = new Label(content);
                l.setWrapText(true);
                l.setStyle("-fx-text-fill: " + TEXT + "; -fx-padding: 2 0 4 14;"
                         + " -fx-font-size: 13;");
                yield l;
            }
            case Detail.Code(var language, var content) -> {
                TextArea code = new TextArea(content);
                code.setEditable(false);
                code.setWrapText(false);
                code.getStyleClass().add("code-block");
                code.setPrefRowCount(Math.min(content.split("\n").length + 1, 12));
                code.setMaxHeight(250);

                Label lang = new Label(language);
                lang.setStyle("-fx-font-size: 10; -fx-text-fill: " + MUTED + ";");

                VBox box = new VBox(2, lang, code);
                box.setPadding(new Insets(2, 0, 4, 14));
                yield box;
            }
            case Detail.Html(var content) -> {
                int lines = content.split("\n").length;
                Label l = new Label("📄 HTML content (" + lines + " lines) — open in web UI to view");
                l.setStyle("-fx-text-fill: " + MUTED + "; -fx-padding: 2 0 4 14;"
                         + " -fx-font-size: 12; -fx-font-style: italic;");
                yield l;
            }
            case Detail.Resource(var path, var type) -> {
                Label l = new Label("📎 " + path + "  [" + type + "]");
                l.setStyle("-fx-text-fill: " + BLUE + "; -fx-padding: 2 0 4 14;"
                         + " -fx-font-size: 13; -fx-font-family: monospace;");
                yield l;
            }
        };
    }

    private static String formatRole(DetailRole role) {
        String name = role.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    private static String roleColor(DetailRole role) {
        return switch (role) {
            case DESCRIPTION -> PINK;
            case EXPECTATION -> GREEN;
            case RATIONALE   -> YELLOW;
            case DESIGN      -> BLUE;
            case EXAMPLE     -> TEXT;
            case PRODUCT     -> PINK;
            case REFERENCE   -> SUB;
            case NOTE        -> MUTED;
        };
    }

    private void addSection(String header, List<String> items, String color) {
        Label h = new Label(header);
        h.setStyle("-fx-font-size: 13; -fx-font-weight: bold;"
                 + " -fx-text-fill: " + SUB + "; -fx-padding: 8 0 4 0;");
        detailContent.getChildren().add(h);
        for (String item : items) {
            Label l = new Label(item);
            l.setStyle("-fx-text-fill: " + color + "; -fx-padding: 2 0 2 14;"
                     + " -fx-font-size: 13;");
            detailContent.getChildren().add(l);
        }
    }

    // ── status bar ───────────────────────────────────────────────

    private HBox buildStatusBar(String[] packages) {
        String pkg = String.join(", ", packages);
        Label status = new Label(pkg + "  ·  " + specCount + " specs  ·  "
                                 + pieceCount + " pieces");
        status.getStyleClass().add("status-label");

        HBox bar = new HBox(status);
        bar.setPadding(new Insets(8, 20, 8, 20));
        bar.getStyleClass().add("status-bar");
        return bar;
    }

    // ── helpers ──────────────────────────────────────────────────

    static String colorFor(NodeInfo info) {
        if (info.piece()) return YELLOW;
        return switch (info.specLevel()) {
            case 1  -> PINK;
            case 2  -> BLUE;
            default -> GREEN;
        };
    }

    private static String badgeClass(NodeInfo info) {
        if (info.piece()) return "badge-piece";
        return switch (info.specLevel()) {
            case 1  -> "badge-l1";
            case 2  -> "badge-l2";
            default -> "badge-l3";
        };
    }

}
