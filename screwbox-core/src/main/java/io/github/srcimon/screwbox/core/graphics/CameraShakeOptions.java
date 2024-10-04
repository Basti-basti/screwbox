package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Configures the shake of the {@link Camera}. Can be applied by {@link Camera#shake(CameraShakeOptions)}.
 *
 * @param duration  the {@link Duration} of the shake
 * @param xStrength the x-strength of the shake
 * @param yStrength the y-strength of the shake
 * @param interval  the {@link Duration} between direction changes (may very to make it more realisitc)
 */
//TODO test
    //TODO document roation
public record CameraShakeOptions(Duration duration, double xStrength, double yStrength, Duration interval, Rotation screenShake) {

    public CameraShakeOptions {
        Validate.zeroOrPositive(xStrength, "strength must be positive");
        Validate.zeroOrPositive(yStrength, "strength must be positive");
    }

    private CameraShakeOptions(final Duration duration) {
        this(duration, 10, 10, Duration.ofMillis(50), Rotation.none());
    }

    /**
     * The {@link Camera} will only stop shaking when invoking {@link Camera#stopShaking()} or applying another shake.
     */
    public static CameraShakeOptions infinite() {
        return new CameraShakeOptions(Duration.none());
    }

    /**
     * The {@link Camera} will only stop shaking after the given {@link Duration}.
     */
    public static CameraShakeOptions lastingForDuration(final Duration duration) {
        return new CameraShakeOptions(duration);
    }

    /**
     * Set the x-strength of the shake. Default: 10.
     */
    public CameraShakeOptions xStrength(final double xStrength) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, screenShake);
    }

    /**
     * Set the y-strength of the shake. Default: 10.
     */
    public CameraShakeOptions yStrength(final double yStrength) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, screenShake);
    }

    /**
     * Set the x- and the y-strength of the shake. Default: 10.
     */
    public CameraShakeOptions strength(final double strength) {
        return new CameraShakeOptions(duration, strength, strength, interval, screenShake);
    }

    /**
     * Set the {@link Duration} between direction changes. Default 50s.
     */
    public CameraShakeOptions interval(final Duration interval) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, screenShake);
    }

    //TODO javadoc test changelog
    public CameraShakeOptions screenRotation(final Rotation screenShake) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, screenShake);
    }
}
