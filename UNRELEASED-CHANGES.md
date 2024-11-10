# 2.6.0

### 🚀 Features & improvements

- Added methods for explizitly rendering `Ui.renderMenu()`, `Scenes.renderTransition()`,
  `Graphics.renderSplitscreenBorders()` so drawing order of these subsystems can be customized (#416)
- Added new entity systems to automate rendering of menus, scene transistions and split screen borders (#416)
- Added option to draw split screens without borders
- Added friction support for Physics entities
- Added option to specify background color of screen in `GraphicsConfiguration` (#425)

### 🪛 Bug Fixes

- Set min size to `Lightmap` to prevent crashes when viewports are too small
- Fixed wrong system order when adding systems using lambdas (#418)
- Avoid unneccesary duplicate drawing of split screen borders
- Added new validation for maximum values

### 🧽 Cleanup & refactoring

- Renamed `SplitscreenOptions`
- Better names for `SystemOrders`
- Added missing JavaDoc to `SplitscreenOptions` and `GraphicsConfiguration`

### 📦 Dependency updates

- Bump Jackson to 2.18.1