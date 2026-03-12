# 1.1.2

## Fixes
- Fixed screenshots being broken on Forge 1.20.1
- Fixed incompatibility with the FirstPerson mod.
- Fixed compatibility issue with the Blur mod on 1.20.1
    - NOTE: Only the Fabric version of the Blur mod works with the workaround.
      On Forge, you have to use the Fabric version of the Blur mod with Sinytra Connector.
- Fixed crash with JourneyMap compatibility mixins on Fabric

# 1.1.1

## Fixes
- Fixed crash on NeoForge 1.21.1, and Forge 1.20.1
- Removed dependency on MixinBooster on Forge 1.20.1, as it is no longer needed any more.

# 1.1.0

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

# 1.0.8

## Fixes
- Fixed Picture Mode crashing the game after switching dimensions on Forge and NeoForge
- Fixed multiplayer related crash on Fabric

# 1.0.7

## Fixes
- Fixed crash on versions older than 1.21.11

# 1.0.6

## Fixes
- Fixed Picture Mode being broken entirely in some instances
- Fixed time of day slider not working on versions older than 1.21.11

# 1.0.5

## Fixes
- Fixed multiplayer related crashes

# 1.0.4

## Fixes
- Fixed crash on Forge 1.20.1

# 1.0.3

## Fixes
- Fixed crash when entering the pause screen pre-maturely
- Fixed crash when entering the world in some instances

# 1.0.2

## Fixes
- Fixed crash with Embeddium on 1.20.1
- Fixed NeoForge 1.21.11 version being broken
- Fixed crash when exiting world on Forge and NeoForge

# 1.0.1

## Changes
- Added short delay for panning after opening the GUI

## Fixes
- Fixed Forge 1.20.1 version being broken

# 1.0.0

Initial release