package io.github.simonbas.screwbox.core.loop.internal;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.loop.Loop;

import java.util.List;

import static io.github.simonbas.screwbox.core.Time.NANOS_PER_SECOND;
import static java.lang.Math.min;

public class DefaultLoop implements Loop {

    private static final int CRITICAL_FPS_COUNT = 10;
    private final List<Updatable> updatables;

    private int fps = 0;
    private long frameNumber;
    private double delta = 0;
    private boolean isRunning = false;
    private Duration updateDuration = Duration.none();
    private Time lastUpdate = Time.now();
    private Time startTime = Time.unset();
    private Duration runningTime = Duration.none();
    private boolean active = false;
    private int targetFps = Loop.MIN_TARGET_FPS;

    public DefaultLoop(final List<Updatable> updatables) {
        this.updatables = updatables;
    }

    public void start() {
        if (active) {
            throw new IllegalStateException("game loop already started");
        }
        active = true;
        startTime = Time.now();
        runGameLoop();
    }

    public void stop() {
        this.active = false;
    }

    @Override
    public Loop setTargetFps(final int targetFps) {
        if (targetFps < Loop.MIN_TARGET_FPS) {
            throw new IllegalArgumentException("target fps must be at least " + Loop.MIN_TARGET_FPS);
        }
        this.targetFps = targetFps;
        return this;

    }

    @Override
    public Loop setUnlimitedFps() {
        return setTargetFps(Integer.MAX_VALUE);
    }

    @Override
    public int targetFps() {
        return targetFps;
    }

    @Override
    public int fps() {
        return fps;
    }

    @Override
    public Duration updateDuration() {
        return updateDuration;
    }

    @Override
    public double delta() {
        return delta;
    }

    @Override
    public Duration runningTime() {
        return runningTime;
    }

    @Override
    public Time lastUpdate() {
        return lastUpdate;
    }

    @Override
    public long frameNumber() {
        return frameNumber;
    }

    private void runGameLoop() {
        isRunning = true;
        try {
            while (active) {
                if (needsUpdate()) {
                    final Time beforeUpdate = Time.now();
                    for (final var updatable : updatables) {
                        updatable.update();
                    }
                    trackUpdateCycle(beforeUpdate);
                } else {
                    beNiceToCpu();
                }
            }
        } finally {
            isRunning = false;
        }
    }

    private boolean needsUpdate() {
        final double targetNanosPerUpdate = Time.NANOS_PER_SECOND * 1.0 / targetFps;
        final double nanosSinceLastUpdate = Duration.since(lastUpdate).nanos();
        return nanosSinceLastUpdate > targetNanosPerUpdate - (updateDuration.nanos() * 1.5);
    }

    private void beNiceToCpu() {
        try {
            Thread.sleep(0, 750_000);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void trackUpdateCycle(final Time beforeUpdate) {
        final Time now = Time.now();
        final Duration timeBetweenUpdates = Duration.between(now, lastUpdate);
        lastUpdate = now;
        fps = (int) (NANOS_PER_SECOND / timeBetweenUpdates.nanos());
        final double maxUpdateFactor = fps <= CRITICAL_FPS_COUNT ? 0 : 1.0 / fps;
        delta = min(timeBetweenUpdates.nanos() * 1.0 / NANOS_PER_SECOND, maxUpdateFactor);
        updateDuration = Duration.between(now, beforeUpdate);
        runningTime = Duration.between(startTime, lastUpdate);
        frameNumber++;
    }

    @Override
    public Time startTime() {
        return startTime;
    }

    public void awaitTermination() {
        while (isRunning) {
            beNiceToCpu();
        }
    }

}
