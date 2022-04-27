package de.suzufa.screwbox.core;

import java.io.Serializable;
import java.util.List;

import de.suzufa.screwbox.core.graphics.world.World;

/**
 * {@link Bounds} represents a space in the 2d world. It's defined by its
 * {@link Bounds#position()} (center of the area) and its {@link #extents()}
 * (the {@link Vector} from the center to it's lower right corner.
 * 
 * The {@link #origin()} defines the upper left corner.
 *
 */
public final class Bounds implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Vector position;
	private final Vector extents;

	/**
	 * Creates a new {@link Bounds} at the given {@link #position()}.
	 * 
	 * @see #atOrigin
	 */
	public static Bounds atPosition(final double x, final double y, final double width, final double height) {
		return new Bounds(x, y, width, height);
	}

	/**
	 * Creates a new {@link Bounds} at the given {@link #position()}.
	 * 
	 * @see #atOrigin
	 */
	public static Bounds atPosition(final Vector position, final double width, final double height) {
		return new Bounds(position.x(), position.y(), width, height);
	}

	/**
	 * Creates a new {@link Bounds} at the given {@link #origin()}.
	 * 
	 * @see #atPosition
	 */
	public static Bounds atOrigin(final Vector origin, final double width, final double height) {
		return atOrigin(origin.x(), origin.y(), width, height);
	}

	/**
	 * Creates a new {@link Bounds} at the given {@link #origin()}.
	 * 
	 * @see #atPosition
	 */
	public static Bounds atOrigin(final double x, final double y, final double width, final double height) {
		return new Bounds(x + (width / 2.0), y + (height / 2.0), width, height);
	}

	/**
	 * Creates a new {@link Bounds} on maximum size and {@link #position()} on the
	 * center of the game {@link World}.
	 */
	public static Bounds max() {
		return atPosition(Vector.zero(), Double.MAX_VALUE, Double.MAX_VALUE);
	}

	/**
	 * Checks if the given position is within this {@link Bounds}.
	 */
	public boolean contains(final Vector position) {
		return minX() <= position.x() && maxX() >= position.x() && minY() <= position.y() && maxY() >= position.y();
	}

	private Bounds(final double x, final double y, final double width, final double height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("size of width and length must no be negative");
		}
		this.position = Vector.of(x, y);
		this.extents = Vector.of(width / 2.0, height / 2.0);
	}

	private Bounds(final Vector position, final Vector extents) {
		this.position = position;
		this.extents = extents;
	}

	public Bounds moveBy(final Vector vector) {
		return moveBy(vector.x(), vector.y());
	}

	public Bounds moveBy(final double x, final double y) {
		return new Bounds(position.add(x, y), extents);
	}

	public Bounds moveTo(final Vector newPosition) {
		return new Bounds(newPosition, extents);
	}

	public Vector origin() {
		return Vector.of(position.x() - extents.x(), position.y() - extents.y());
	}

	public Vector size() {
		return Vector.of(width(), height());
	}

	public Bounds inflated(final double inflation) {
		return Bounds.atPosition(position.x(), position.y(), width() + inflation, height() + inflation);
	}

	/**
	 * Checks if this {@link Bounds} touches another {@link Bounds}. Is also
	 * {@code true} if it {@link #intersects(Bounds)} the other {@link Bounds}.
	 * 
	 * @see #intersects(Bounds)
	 */
	public boolean touches(final Bounds other) {
		return inflated(0.001).intersects(other);
	}

	/**
	 * Checks if this {@link Bounds} intersects another {@link Bounds}.
	 * 
	 * @see #touches(Bounds)
	 */
	public boolean intersects(final Bounds other) {
		return maxX() > other.minX() && minX() < other.maxX() && maxY() > other.minY() && minY() < other.maxY();
	}

	/**
	 * Returns the width of this {@link Bounds}.
	 */
	public double width() {
		return extents.x() * 2.0;
	}

	/**
	 * Returns the height of this {@link Bounds}.
	 */
	public double height() {
		return extents.y() * 2.0;
	}

	/**
	 * Calculates the area of overlap between this {@link Bounds} and the other
	 * {@link Bounds}.
	 */
	public double overlapArea(final Bounds other) {
		final double xOverlap = Math.max(0, Math.min(maxX(), other.maxX()) - Math.max(minX(), other.minX()));
		if (xOverlap == 0) { // tweak performance: must not calculate yOverlap
			return 0;
		}
		final double yOverlap = Math.max(0, Math.min(maxY(), other.maxY()) - Math.max(minY(), other.minY()));
		return xOverlap * yOverlap;
	}

	public Vector extents() {
		return extents;
	}

	public double minX() {
		return position.x() - extents.x();
	}

	public double maxX() {
		return position.x() + extents.x();
	}

	public double minY() {
		return position.y() - extents.y();
	}

	public double maxY() {
		return position.y() + extents.y();
	}

	@Override
	public String toString() {
		return "Bounds [position=" + position + ", size=" + size() + "]";
	}

	public Vector position() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((extents == null) ? 0 : extents.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Bounds other = (Bounds) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (extents == null) {
			if (other.extents != null)
				return false;
		} else if (!extents.equals(other.extents))
			return false;
		return true;
	}

	public Vector bottomLeft() {
		return Vector.of(minX(), maxY());
	}

	public Vector bottomRight() {
		return Vector.of(maxX(), maxY());
	}

	public Vector topRight() {
		return Vector.of(maxX(), minY());
	}

	public List<Vector> corners() {
		return List.of(origin(), bottomLeft(), bottomRight(), topRight());
	}

	public Segment top() {
		return Segment.between(origin(), topRight());
	}

	public Segment right() {
		return Segment.between(bottomRight(), topRight());
	}

	public Segment bottom() {
		return Segment.between(bottomRight(), bottomLeft());
	}

	public Segment left() {
		return Segment.between(bottomLeft(), origin());
	}

}
