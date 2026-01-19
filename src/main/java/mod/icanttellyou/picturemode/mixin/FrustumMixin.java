package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "offsetToFullyIncludeCameraCube", at = @At("HEAD"), cancellable = true)
    private void bypassOffsettingIfInPM(int offset, CallbackInfoReturnable<Frustum> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled())
            return;

        cir.setReturnValue((Frustum) (Object) this);
    }
}
