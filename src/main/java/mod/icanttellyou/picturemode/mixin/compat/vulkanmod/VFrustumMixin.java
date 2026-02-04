package mod.icanttellyou.picturemode.mixin.compat.vulkanmod;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "net.vulkanmod.render.chunk.frustum.VFrustum", remap = false)
public abstract class VFrustumMixin {
    @Inject(method = "offsetToFullyIncludeCameraCube", at = @At("HEAD"), cancellable = true)
    private void bypassOffsettingIfInPM(int offset, @Coerce CallbackInfoReturnable<Object> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        cir.setReturnValue(this);
    }
}
