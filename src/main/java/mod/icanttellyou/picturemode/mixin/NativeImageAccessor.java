//? if <1.21.5 {
/*package mod.icanttellyou.picturemode.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NativeImage.class)
public interface NativeImageAccessor {
    //? if <1.21.5 {
    /^@Accessor("pixels")
    long getPixels();
    ^///? }
}
*///? }