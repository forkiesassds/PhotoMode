//? if >=1.21.9 {
package mod.icanttellyou.picturemode.mixin.compat.journeymap;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "journeymap.client.ui.debug.DebugEntry", remap = false)
public abstract class DebugEntryMixin {
    @Inject(method = "display", at = @At("HEAD"), cancellable = true)
    private void disableJMDebugStatsInPM(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }
}
//? }
