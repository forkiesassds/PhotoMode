package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.services.PictureModeServices;

public class ModStatus {
    /* Mods that need workarounds to fix issues */
    public static final boolean HAS_JOURNEYMAP = PictureModeServices.PLATFORM.isModPresent("journeymap");
    public static final boolean HAS_NOSTALGIC_TWEAKS = PictureModeServices.PLATFORM.isModPresent("nostalgic_tweaks");

    /* Rendering improvement/overhaul mods */
    public static final boolean HAS_SODIUM = PictureModeServices.PLATFORM.isModPresent("sodium");
    public static final boolean HAS_VULKANMOD = PictureModeServices.PLATFORM.isModPresent("vulkanmod");

    /* Mods that improve visuals */
    //? if >=1.21.6
    public static final boolean HAS_VOXY = PictureModeServices.PLATFORM.isModPresent("voxy");
}
