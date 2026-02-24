## Additions
- Added filters, a new option for enhancing screenshots you take.
- Added a configuration screen, if YACL is installed.
- Added options to change output file type for screenshots.
- Added screenshot fixed size and resolution multiplier options
- Added a keybind to open Picture Mode, being set to `M` by default.

## Changes
- Made the GUI not disappear briefly when taking a screenshot.
  - NOTE: Due to how rendering works on 1.21.1 and older,
    a slight flicker will still appear when taking a screenshot on these versions.

## Fixes
- Fixed clouds appearing with Fabulous graphics on 1.21.1 and older.
- Fixed game crash on Fabric when client gets forcefully disconnected from a multiplayer server.
- Fixed incompatibilities with VulkanMod
  - NOTE: VulkanMod compatibility is experimental, and you ~~may~~ WILL encounter issues.
    Most of these issues are caused by VulkanMod, and not by Picture Mode.
- Fixed JourneyMap appearing in Picture Mode, and in screenshots.
- Fixed toasts appearing in Picture Mode, and in screenshots.
- Fixed broken compatibility with Nostalgic Tweaks