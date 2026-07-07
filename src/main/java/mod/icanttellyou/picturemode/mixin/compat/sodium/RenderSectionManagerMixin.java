//? if <26.3 {
package mod.icanttellyou.picturemode.mixin.compat.sodium;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = {
    //? if >=1.21
    "net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager"
    /*? if >=1.21 && <=1.21.1 {*//*, *//*? }*/
    //? if <=1.21.1
    //"me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager"
}, remap = false)
public abstract class RenderSectionManagerMixin {
    @Inject(method = "shouldUseOcclusionCulling", at = @At("RETURN"), cancellable = true, require = 0)
    private void disableSmartCullInPM(CallbackInfoReturnable<Boolean> cir) {
        PictureModeState state = PictureModeClient.getState();
        boolean original = cir.getReturnValueZ();
        cir.setReturnValue((state == null || !state.isEnabled()) && original);
    }
}
//? }