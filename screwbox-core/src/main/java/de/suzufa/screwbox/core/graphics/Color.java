package de.suzufa.screwbox.core.graphics;

import de.suzufa.screwbox.core.Percentage;

/**
 * Used to store color information used in {@link Graphics}.
 */
public final class Color {

    private final int r;
    private final int g;
    private final int b;
    private final Percentage opacity;

    /**
     * The color black.
     */
    public static final Color BLACK = Color.rgb(0, 0, 0);

    /**
     * The color white.
     */
    public static final Color WHITE = Color.rgb(255, 255, 255);

    /**
     * The color red.
     */
    public static final Color RED = Color.rgb(255, 0, 0);

    /**
     * The color green.
     */
    public static final Color GREEN = Color.rgb(0, 255, 0);

    /**
     * The color blue.
     */
    public static final Color BLUE = Color.rgb(0, 0, 255);

    /**
     * The color yellow.
     */
    public static final Color YELLOW = Color.rgb(255, 255, 0);

    /**
     * Creates a color based on RGB-components with full {@link #opacity()}.
     */
    public static Color rgb(final int r, final int g, final int b) {
        return new Color(r, g, b);
    }

    /**
     * Creates a color based on RGB-components and custom {@link #opacity()}.
     */
    public static Color rgb(final int r, final int g, final int b, Percentage opacity) {
        return new Color(r, g, b, opacity);
    }

    /**
     * Creates a new instance with same RGB-components, but custom
     * {@link #opacity()}.
     */
    public Color withOpacity(Percentage opacity) {
        return new Color(r, g, b, opacity);
    }

    /**
     * Returns red value of the {@link Color}.
     */
    public int r() {
        return r;
    }

    /**
     * Returns green value of the {@link Color}.
     */
    public int g() {
        return g;
    }

    /**
     * Returns blue value of the {@link Color}.
     */
    public int b() {
        return b;
    }

    /**
     * Returns the colors opacity value.
     */
    public Percentage opacity() {
        return opacity;
    }

    private Color(final int r, final int g, final int b) {
        this(r, g, b, Percentage.max());
    }

    private Color(final int r, final int g, final int b, Percentage opacity) {
        validate(r);
        validate(g);
        validate(b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
    }

    private void validate(final int rgbValue) {
        if (rgbValue < 0 || rgbValue > 255) {
            throw new IllegalArgumentException("invalid color value (0-255): " + rgbValue);
        }
    }
}
