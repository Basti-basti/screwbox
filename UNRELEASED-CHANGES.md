## v1.0.0-RC6

### 🚀 Features & improvements

- Introduced `TweenMode` with different options to create beautiful tweens (#164)
- Added position tweening
- Added `Line.normal(position, length)` (#169)
- Added `Sound.format()`
- Added `Keyboard.wsadMovement(length)` and `.arrowKeyMovement(length)`

### 🪛 Bug Fixes

- Fixed unhandled exceptions in render thread (#166)
- Fixed cause of unhandled exception in render thread (light radius is zero) (#166)
- Fixed loading midi sounds (#168)
- Fixed invalid result of  `Vector.zero().adjustLength(length)` returns invalid data

### 🧽 Cleanup & refactoring

- Renamed `Vector.adjustLengthTo(length)` to `.length(length)`

### 📦 Dependency updates

- ...