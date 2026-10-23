package jarista.web;

import hue.captains.singapura.js.homing.studio.base.composed.CodeSegment;
import hue.captains.singapura.js.homing.studio.base.composed.ComposedDoc;
import hue.captains.singapura.js.homing.studio.base.composed.MarkdownSegment;
import hue.captains.singapura.js.homing.studio.base.composed.RigidSegment;
import hue.captains.singapura.js.homing.studio.base.composed.Segment;
import jarista.detail.Detail;
import jarista.detail.DetailRole;
import jarista.detail.RoledDetail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Maps a Jarista node's typed {@link RoledDetail} blocks onto Homing segments —
 * one per detail, in declaration order, each captioned with its {@link DetailRole}.
 *
 * <p>Every Jarista {@link Detail} variant collapses onto just two segment types,
 * {@link MarkdownSegment} (prose, and — via inline HTML — SVG, HTML, and base64
 * images) and {@link CodeSegment}. Both are {@link RigidSegment}s, so the same
 * mapping feeds a standalone {@link ComposedDoc} (catalogue leaves) and a node
 * body inside the RigidTree ({@link JaristaRigidTree}).
 */
final class DetailToDoc {

    private DetailToDoc() {}

    /** A standalone ComposedDoc for a node (used for catalogue doc leaves). */
    static ComposedDoc build(String uuidKey, String title, String summary,
                             String category, String meta, List<RoledDetail> details) {
        List<Segment> segments = new ArrayList<>(rigidSegments(meta, details));
        return ComposedDoc.of(
                uuidFor(uuidKey),
                title,
                (summary == null || summary.isBlank()) ? title : summary,
                category,
                segments);
    }

    /**
     * The node's body as rigid segments: an optional leading "at a glance" block,
     * then one segment per detail. Never empty — a placeholder stands in when a
     * node has nothing recorded, so every tree node still renders a body.
     */
    static List<RigidSegment> rigidSegments(String meta, List<RoledDetail> details) {
        List<RigidSegment> segments = new ArrayList<>();
        if (meta != null && !meta.isBlank()) {
            segments.add(new MarkdownSegment(meta, Optional.of("At a glance")));
        }
        for (RoledDetail rd : details) {
            segments.add(toSegment(rd));
        }
        if (segments.isEmpty()) {
            segments.add(new MarkdownSegment(
                    "_No details recorded for this node yet._", Optional.empty()));
        }
        return segments;
    }

    private static RigidSegment toSegment(RoledDetail rd) {
        String caption = roleLabel(rd.role());
        Detail d = rd.detail();
        return switch (d) {
            case Detail.Text(var content) ->
                    new MarkdownSegment(content, Optional.of(caption));
            case Detail.Code(var language, var content) ->
                    new CodeSegment(content, language, Optional.of(caption));
            case Detail.Html(var content) ->
                    new MarkdownSegment(content, Optional.of(caption));
            case Detail.Svg(var content) ->
                    new MarkdownSegment(svgBlock(content), Optional.of(caption));
            case Detail.Resource(var path, var type) ->
                    resourceSegment(path, type, caption);
        };
    }

    private static RigidSegment resourceSegment(String path, jarista.detail.ResourceType type, String caption) {
        return switch (type) {
            case SVG -> {
                String svg = readText(path);
                yield svg == null
                        ? missing(path, caption)
                        : new MarkdownSegment(svgBlock(svg), Optional.of(caption));
            }
            case HTML -> {
                String html = readText(path);
                yield html == null
                        ? missing(path, caption)
                        : new MarkdownSegment(html, Optional.of(caption));
            }
            case MARKDOWN -> {
                String md = readText(path);
                yield md == null
                        ? missing(path, caption)
                        : new MarkdownSegment(md, Optional.of(caption));
            }
            case PLAIN_TEXT -> {
                String txt = readText(path);
                yield txt == null
                        ? missing(path, caption)
                        : new CodeSegment(txt, "text", Optional.of(caption));
            }
            case PNG -> {
                String dataUrl = readImageDataUrl(path, "image/png");
                yield dataUrl == null
                        ? missing(path, caption)
                        : new MarkdownSegment("![" + path + "](" + dataUrl + ")", Optional.of(caption));
            }
            case PDF -> new MarkdownSegment(
                    "📎 Resource: `" + path + "` (PDF — open the file directly).",
                    Optional.of(caption));
        };
    }

    /** Wrap raw SVG so it renders as a centered block through marked's inline-HTML pass. */
    private static String svgBlock(String svg) {
        return "<div class=\"jarista-svg\" style=\"text-align:center;overflow-x:auto\">\n"
                + svg + "\n</div>";
    }

    private static RigidSegment missing(String path, String caption) {
        return new MarkdownSegment("_(resource not found on classpath: `" + path + "`)_",
                Optional.of(caption));
    }

    private static String readText(String classpathPath) {
        try (InputStream in = DetailToDoc.class.getResourceAsStream("/" + classpathPath)) {
            if (in == null) return null;
            return new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    private static String readImageDataUrl(String classpathPath, String mime) {
        try (InputStream in = DetailToDoc.class.getResourceAsStream("/" + classpathPath)) {
            if (in == null) return null;
            String b64 = java.util.Base64.getEncoder().encodeToString(in.readAllBytes());
            return "data:" + mime + ";base64," + b64;
        } catch (Exception e) {
            return null;
        }
    }

    /** Title-case a DetailRole for use as a segment caption (DESCRIPTION -> "Description"). */
    static String roleLabel(DetailRole role) {
        String n = role.name().toLowerCase(java.util.Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }

    /** Deterministic UUID for a node key. */
    static UUID uuidFor(String key) {
        return ComposedDoc.deterministicUuid("jarista:" + key);
    }
}
