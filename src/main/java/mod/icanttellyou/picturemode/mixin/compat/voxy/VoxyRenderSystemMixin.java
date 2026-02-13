//? if >=1.21.6 {
package mod.icanttellyou.picturemode.mixin.compat.voxy;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "me.cortex.voxy.client.core.VoxyRenderSystem", remap = false)
public abstract class VoxyRenderSystemMixin {
    @Inject(method = "makeProjectionMatrix", at = @At("HEAD"), cancellable = true)
    private static void setupPMMatrices(float near, float far, CallbackInfoReturnable<Matrix4f> cir) {
        Minecraft client = Minecraft.getInstance();
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        int width = client.getWindow().getWidth();
        int height = client.getWindow().getHeight();

        double delta = client.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        cir.setReturnValue(state.getProjectionMatrix(width, height, near, far, delta));
    }
}
//? }