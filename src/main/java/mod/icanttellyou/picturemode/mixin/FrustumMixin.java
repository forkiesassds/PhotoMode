package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "offsetToFullyIncludeCameraCube", at = @At("HEAD"), cancellable = true)
    private void bypassOffsettingIfNotPossibleInPM(int offset, CallbackInfoReturnable<Frustum> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled())
            return;

        Minecraft mc = Minecraft.getInstance();
        double delta = /*? >=1.21 {*/ mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*mc.getFrameTime() *//*?}*/;

        double width = state.adjustViewportDimension(mc.getWindow().getWidth(), delta);
        double height = state.adjustViewportDimension(mc.getWindow().getHeight(), delta);

        if (width < offset * Frustum.OFFSET_STEP || height < offset * Frustum.OFFSET_STEP) {
            cir.setReturnValue((Frustum) (Object) this);
        }
    }
}
