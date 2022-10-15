package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.List;

import de.suzufa.screwbox.core.graphics.internal.ImageUtil;

//TODO: javadoc and test
public class Lightmap implements AutoCloseable {

    private static final java.awt.Color[] COLORS = new java.awt.Color[] {
            toAwtColor(Color.BLACK),
            toAwtColor(Color.TRANSPARENT) };

    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int resolution;

    public Lightmap(final Dimension size, final int resolution) {
        this.image = new BufferedImage(
                size.width() / resolution,
                size.height() / resolution,
                BufferedImage.TYPE_INT_ARGB);
        this.resolution = resolution;
        this.graphics = (Graphics2D) image.getGraphics();
    }

    public Lightmap addPointLight(final Offset position, final int range, final List<Offset> area) {
        float[] fractions = new float[2];
        fractions[0] = 0f;
        fractions[1] = 1f;

        Polygon polygon = new Polygon();
        for (var node : area) {
            polygon.addPoint(node.x() / resolution, node.y() / resolution);
        }
        RadialGradientPaint paint = new RadialGradientPaint(
                position.x() / resolution, position.y() / resolution,
                range / resolution,
                fractions, COLORS);

        graphics.setPaint(paint);
        graphics.fillPolygon(polygon);
        return this;
    }

    public Sprite createImage() {
        Image result = ImageUtil.applyFilter(image, new InvertAlphaFilter());
        return Sprite.fromImage(result);
    }

    @Override
    public void close() {
        graphics.dispose();

    }
}
