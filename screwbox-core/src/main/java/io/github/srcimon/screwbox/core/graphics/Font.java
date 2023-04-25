package io.github.srcimon.screwbox.core.graphics;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Font {

    public enum Style {
        NORMAL,
        ITALIC,
        BOLD,
        ITALIC_BOLD
    }

    private final String name;
    private final int size;
    private final Style style;

    public Font(final String name, final int size) {
        this(name, size, Style.NORMAL);
    }

    public Font(final String name, final int size, final Style style) {
        if (size <= 0) {
            throw new IllegalArgumentException("font must have size greater 0");
        }
        this.name = requireNonNull(name, "font name must not be null");
        this.style = requireNonNull(style, "font style must not be null");
        this.size = size;
    }

    public String name() {
        return name;
    }

    public int size() {
        return size;
    }

    public Style style() {
        return style;
    }

    public Font withSize(final int size) {
        return new Font(name, size, style);
    }

}
