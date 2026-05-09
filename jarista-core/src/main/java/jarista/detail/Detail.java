package jarista.detail;

/**
 * A typed content block — purely about <em>what</em> the content is,
 * not <em>why</em> it exists (that's {@link DetailRole}).
 *
 * <p>Four variants:
 * <ul>
 *   <li>{@link Text}     — prose, rendered as-is.</li>
 *   <li>{@link Code}     — source code with a language tag.</li>
 *   <li>{@link Html}     — inline HTML/SVG, rendered via a web view.</li>
 *   <li>{@link Resource} — reference to a static file (.md, .svg, .png, …).</li>
 * </ul>
 */
public sealed interface Detail {

    /** Inline prose — plain or lightly formatted text. */
    record Text(String content) implements Detail {}

    /** Inline source code with a language tag for syntax highlighting. */
    record Code(String language, String content) implements Detail {}

    /** Inline HTML content — rendered via WebView. Supports SVG, styled markup, etc. */
    record Html(String content) implements Detail {}

    /** Reference to a static resource on the classpath or project tree. */
    record Resource(String path, ResourceType type) implements Detail {}
}
