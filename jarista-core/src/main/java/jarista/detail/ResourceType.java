package jarista.detail;

/**
 * Content type of a resource referenced by {@link Detail.Resource}.
 * Tells the UI layer how to render or embed the resource.
 */
public enum ResourceType {

    MARKDOWN,
    SVG,
    PNG,
    PDF,
    HTML,
    PLAIN_TEXT
}
