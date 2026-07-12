package jarista.detail;

/**
 * Fluent DSL for composing individual {@link RoledDetail} instances.
 *
 * <pre>
 *   private static final DetailBuilder _db = new DetailBuilder();
 *
 *   public List&lt;RoledDetail&gt; details() {
 *       return List.of(
 *           _db.text()
 *               .ln("Graph-native PM for Java.")
 *               .ln("No YAML, no dashboards.")
 *           .as(DESCRIPTION),
 *
 *           _db.code("java")
 *               .ln("public class MySpec implements L3_Spec&lt;Parent&gt; {}")
 *           .as(EXAMPLE),
 *
 *           _db.resource("docs/design.md", ResourceType.MARKDOWN)
 *           .as(REFERENCE)
 *       );
 *   }
 * </pre>
 */
public final class DetailBuilder {

    /** Start building inline text content. */
    public TextBlock text() {
        return new TextBlock();
    }

    /** Start building a code block with the given language tag. */
    public CodeBlock code(String language) {
        return new CodeBlock(language);
    }

    /** Start building inline HTML content. */
    public HtmlBlock html() {
        return new HtmlBlock();
    }

    /** Start building inline SVG content. */
    public SvgBlock svg() {
        return new SvgBlock();
    }

    /** Start building a resource reference. */
    public ResourceRef resource(String path, ResourceType type) {
        return new ResourceRef(path, type);
    }

    /** Shortcut: SVG resource reference. */
    public ResourceRef svg(String path) {
        return new ResourceRef(path, ResourceType.SVG);
    }

    // ── inner block builders ─────────────────────────────────────

    public static final class TextBlock {
        private final StringBuilder sb = new StringBuilder();

        public TextBlock ln(String line) {
            if (!sb.isEmpty()) sb.append('\n');
            sb.append(line);
            return this;
        }

        /** Tag this text block with a role, producing a {@link RoledDetail}. */
        public RoledDetail as(DetailRole role) {
            return new RoledDetail(role, new Detail.Text(sb.toString()));
        }
    }

    public static final class CodeBlock {
        private final String language;
        private final StringBuilder sb = new StringBuilder();

        CodeBlock(String language) { this.language = language; }

        public CodeBlock ln(String line) {
            if (!sb.isEmpty()) sb.append('\n');
            sb.append(line);
            return this;
        }

        /** Tag this code block with a role, producing a {@link RoledDetail}. */
        public RoledDetail as(DetailRole role) {
            return new RoledDetail(role, new Detail.Code(language, sb.toString()));
        }
    }

    public static final class HtmlBlock {
        private final StringBuilder sb = new StringBuilder();

        public HtmlBlock ln(String line) {
            if (!sb.isEmpty()) sb.append('\n');
            sb.append(line);
            return this;
        }

        /** Tag this HTML block with a role, producing a {@link RoledDetail}. */
        public RoledDetail as(DetailRole role) {
            return new RoledDetail(role, new Detail.Html(sb.toString()));
        }
    }

    public static final class SvgBlock {
        private final StringBuilder sb = new StringBuilder();

        public SvgBlock ln(String line) {
            if (!sb.isEmpty()) sb.append('\n');
            sb.append(line);
            return this;
        }

        /** Tag this SVG block with a role, producing a {@link RoledDetail}. */
        public RoledDetail as(DetailRole role) {
            return new RoledDetail(role, new Detail.Svg(sb.toString()));
        }
    }

    public static final class ResourceRef {
        private final String path;
        private final ResourceType type;

        ResourceRef(String path, ResourceType type) {
            this.path = path;
            this.type = type;
        }

        /** Tag this resource reference with a role, producing a {@link RoledDetail}. */
        public RoledDetail as(DetailRole role) {
            return new RoledDetail(role, new Detail.Resource(path, type));
        }
    }
}
