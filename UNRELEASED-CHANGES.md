### 🚀 Features & improvements

- Added `Graphics.renderDuration()` (#265)
- Support for horizontal and vertical spinning sprites (#311)
- Added `TweenSpinComponent` / `System` to link spin of a sprite to the entities tween
- Added `FixedSpinComponent` / `System` to add constant spin animation to a sprite
- Added `Percent.addWithOverflow(double)`
- Added `ParticleOptions.animateHorizontalSpin()`, `.animateVerticalSpin()`

### 🪛 Bug Fixes

- Changed min delta on loop on very high load to 0.01 to prevent stalling of engine
- Moved back to ubuntu-latest for Github action build (#374)

### 🧽 Cleanup & refactoring

- Improved performance for calculating light areas
- Significant performance improvement of collision detection, resolve and magnets
- Time used for calculating Archetype hashes reduced by 25%
- Renamed `Rotation.ofMovement(double, double)`

### 📦 Dependency updates

- Bump Junit to 5.11.1
- Bump Mockito to 5.14.1
- Bump Jackson to 2.18.0