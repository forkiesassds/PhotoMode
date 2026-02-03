package mod.icanttellyou.picturemode;

import net.minecraft.resources.Identifier;

public class PictureMode {
    public static final String MOD_ID = "picturemode";
    public static final String MOD_NAME = "Picture Mode";

    public static Identifier createId(String name) {
        return Identifier./*? >=1.21 {*/fromNamespaceAndPath/*?} else {*//*tryBuild*//*?}*/(MOD_ID, name);
    }
}
