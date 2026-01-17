package mod.icanttellyou.picturemode.mixin.compat.sodium;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = {
    //? if >=1.21
    "net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer"
    /*? if >=1.21 && <=1.21.1 {*//*, *//*? }*/
    //? if <=1.21.1
    //"me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer"
}, remap = false)
public abstract class SodiumWorldRendererMixin {
    @ModifyVariable(method = "setupTerrain", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return !state.isEnabled() && original;
    }
}