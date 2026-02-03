package mod.icanttellyou.picturemode;

import mod.icanttellyou.picturemode.services.PictureModeServices;
import net.minecraft.resources.Identifier;

public class PictureMode {
    public static final String MOD_ID = "picturemode";
    public static final String MOD_NAME = "Picture Mode";

    public static final boolean HAS_SODIUM = PictureModeServices.PLATFORM.isModPresent("sodium");
    public static final boolean HAS_VULKANMOD = PictureModeServices.PLATFORM.isModPresent("vulkanmod");

    public static Identifier createId(String name) {
        return Identifier./*? >=1.21 {*/fromNamespaceAndPath/*?} else {*//*tryBuild*//*?}*/(MOD_ID, name);
    }
}
