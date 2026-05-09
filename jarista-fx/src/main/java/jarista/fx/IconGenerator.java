package jarista.fx;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Generates Jarista app icon PNGs using Java2D.
 *
 * <p>Coffee palette — Jarista is Java + Barista.
 * Flat nodes with varied shapes (circle, rounded-square, diamond, hexagon)
 * on a woven espresso background.
 * 结图为谱 — weave the graph into a score.
 *
 * <pre>
 *   mvn -pl jarista-fx exec:java -Dexec.mainClass=jarista.fx.IconGenerator
 * </pre>
 */
public final class IconGenerator {

    // ── coffee palette ───────────────────────────────────────────
    private static final Color BG       = new Color(0x23, 0x19, 0x11);
    private static final Color WEAVE_A  = new Color(0x30, 0x23, 0x18);
    private static final Color WEAVE_B  = new Color(0x3D, 0x2D, 0x20);
    private static final Color BORDER   = new Color(0x5C, 0x44, 0x33);
    private static final Color EDGE     = new Color(0x8B, 0x73, 0x55);

    // flat node fills — warm coffee tones, subtle variation
    private static final Color NODE_FILL = new Color(0xD4, 0xB8, 0x96);   // crema
    private static final Color NODE_RIM  = new Color(0x6F, 0x56, 0x3E);   // dark rim
    // material highlight (top edge catch-light)
    private static final Color MAT_HI   = new Color(0xF0, 0xE0, 0xC8, 100);
    // material shadow (bottom edge)
    private static final Color MAT_LO   = new Color(0x3A, 0x28, 0x18, 100);

    public static void main(String[] args) throws Exception {
        String dir = "jarista-fx/src/main/resources";
        new File(dir).mkdirs();

        for (int size : new int[]{256, 64, 32, 16}) {
            BufferedImage img = generate(size);
            File out = new File(dir, "jarista-icon-" + size + ".png");
            ImageIO.write(img, "png", out);
            System.out.println("Wrote " + out.getAbsolutePath());
        }
    }

    static BufferedImage generate(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        double s = size / 256.0;
        Shape clip = new RoundRectangle2D.Double(0, 0, size, size, 48 * s, 48 * s);

        // ── espresso background ──────────────────────────────────
        g.setClip(clip);
        g.setColor(BG);
        g.fill(clip);

        // ── weave texture ────────────────────────────────────────
        int step = Math.max((int)(22 * s), 3);
        float sw = (float)(1.8 * s);
        for (int i = -size; i < size * 2; i += step) {
            g.setColor((i / step & 1) == 0 ? WEAVE_A : WEAVE_B);
            g.setStroke(new BasicStroke(sw));
            g.draw(new Line2D.Double(i, 0, i + size, size));
        }
        for (int i = -size; i < size * 2; i += step) {
            g.setColor((i / step & 1) == 0 ? WEAVE_B : WEAVE_A);
            g.setStroke(new BasicStroke(sw));
            g.draw(new Line2D.Double(i + size, 0, i, size));
        }

        // ── border ───────────────────────────────────────────────
        g.setClip(null);
        g.setColor(BORDER);
        g.setStroke(new BasicStroke((float)(3 * s)));
        g.draw(new RoundRectangle2D.Double(2 * s, 2 * s,
                size - 4 * s, size - 4 * s, 44 * s, 44 * s));
        g.setClip(clip);

        // ── node positions & shapes ──────────────────────────────
        //  L1:  circle          (128, 56)   r=28
        //  L2L: rounded square  ( 72, 136)  r=22
        //  L2R: diamond         (184, 136)  r=22
        //  L3A: hexagon         ( 40, 208)  r=18
        //  L3B: circle          (118, 208)  r=18
        //  L3C: rounded square  (216, 208)  r=18

        // ── edges ────────────────────────────────────────────────
        g.setColor(EDGE);
        g.setStroke(new BasicStroke((float)(4 * s), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawLine(g, s, 128, 56, 72, 136);
        drawLine(g, s, 128, 56, 184, 136);
        drawLine(g, s, 72, 136, 40, 208);
        drawLine(g, s, 72, 136, 118, 208);
        drawLine(g, s, 184, 136, 216, 208);

        // ── nodes ────────────────────────────────────────────────
        drawFlatNode(g, s, circle(128, 56, 28, s));
        drawFlatNode(g, s, roundedSquare(72, 136, 22, s));
        drawFlatNode(g, s, diamond(184, 136, 22, s));
        drawFlatNode(g, s, hexagon(40, 208, 18, s));
        drawFlatNode(g, s, circle(118, 208, 18, s));
        drawFlatNode(g, s, roundedSquare(216, 208, 18, s));

        g.dispose();
        return img;
    }

    // ── flat material node ───────────────────────────────────────

    private static void drawFlatNode(Graphics2D g, double s, Shape shape) {
        Shape savedClip = g.getClip();
        Rectangle2D b = shape.getBounds2D();

        // rim (shadow outline)
        AffineTransform shift = AffineTransform.getTranslateInstance(1.5 * s, 2 * s);
        g.setColor(NODE_RIM);
        g.fill(shift.createTransformedShape(shape));

        // continuous gradient: highlight at top → base fill → shadow at bottom
        LinearGradientPaint grad = new LinearGradientPaint(
                (float) b.getCenterX(), (float) b.getMinY(),
                (float) b.getCenterX(), (float) b.getMaxY(),
                new float[]{0f, 0.35f, 0.7f, 1f},
                new Color[]{
                        new Color(0xF0, 0xE0, 0xC8),   // warm highlight
                        NODE_FILL,                       // crema
                        NODE_FILL,                       // crema
                        new Color(0x9A, 0x80, 0x60)     // warm shadow
                });
        g.setPaint(grad);
        g.fill(shape);

        // restore original clip (icon rounded-rect boundary)
        g.setClip(savedClip);
    }

    // ── shape factories ──────────────────────────────────────────

    private static Shape circle(double cx, double cy, double r, double s) {
        return new Ellipse2D.Double((cx - r) * s, (cy - r) * s, r * 2 * s, r * 2 * s);
    }

    private static Shape roundedSquare(double cx, double cy, double r, double s) {
        double side = r * 1.7;
        double corner = r * 0.5;
        return new RoundRectangle2D.Double(
                (cx - side / 2) * s, (cy - side / 2) * s,
                side * s, side * s, corner * s, corner * s);
    }

    private static Shape diamond(double cx, double cy, double r, double s) {
        double d = r * 1.15;
        Path2D p = new Path2D.Double();
        p.moveTo(cx * s, (cy - d) * s);       // top
        p.lineTo((cx + d) * s, cy * s);       // right
        p.lineTo(cx * s, (cy + d) * s);       // bottom
        p.lineTo((cx - d) * s, cy * s);       // left
        p.closePath();
        return p;
    }

    private static Shape hexagon(double cx, double cy, double r, double s) {
        Path2D p = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            if (i == 0) p.moveTo(x * s, y * s);
            else        p.lineTo(x * s, y * s);
        }
        p.closePath();
        return p;
    }

    private static void drawLine(Graphics2D g, double s,
                                  double x1, double y1, double x2, double y2) {
        g.draw(new Line2D.Double(x1 * s, y1 * s, x2 * s, y2 * s));
    }
}
