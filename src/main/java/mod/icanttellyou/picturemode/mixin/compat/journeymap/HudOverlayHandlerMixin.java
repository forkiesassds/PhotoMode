package mod.icanttellyou.picturemode.mixin.compat.journeymap;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "journeymap.client.event.handlers.HudOverlayHandler", remap = false)
public abstract class HudOverlayHandlerMixin {
    @Inject(method = "preOverlay", at = @At("HEAD"), cancellable = true)
    private void hideJMInPM$posePre(CallbackInfoReturnable<Boolean> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            cir.setReturnValue(false);
    }

    @Inject(method = "postOverlay", at = @At("HEAD"), cancellable = true)
    private void hideJMInPM$posePost(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }

    //? if <1.21.9 {
    /*@Inject(method = "onRenderOverlayDebug", at = @At("HEAD"), cancellable = true)
    private void disableJMDebugStatsInPM(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }
    *///? }

    @Inject(method = "onRenderOverlay", at = @At("HEAD"), cancellable = true)
    private void hideJMInPM$render(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }
}
