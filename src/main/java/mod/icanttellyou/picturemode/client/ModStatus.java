package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.services.PictureModeServices;

public class ModStatus {
    public static final boolean HAS_JOURNEYMAP = PictureModeServices.PLATFORM.isModPresent("journeymap");

    public static final boolean HAS_SODIUM = PictureModeServices.PLATFORM.isModPresent("sodium");
    public static final boolean HAS_VULKANMOD = PictureModeServices.PLATFORM.isModPresent("vulkanmod");
}
