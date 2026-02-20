package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if <1.21.6 {
/*import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    //? if <1.21.6 {
    /*@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void hideSkyInPM(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }
    *///? }

    @WrapOperation(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"
        )
    )
    private CloudStatus hideCloudsInPM(Options instance, Operation<CloudStatus> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || state.isEnabled()) {
            //? if <1.21.2 {
            /*if (net.minecraft.client.Minecraft.useShaderTransparency()) {
                com.mojang.blaze3d.pipeline.RenderTarget renderTarget = ((LevelRenderer) (Object) this).getCloudsTarget();
                if (renderTarget != null) {
                    renderTarget.clear(net.minecraft.client.Minecraft.ON_OSX);
                }
            }
            *///? }

            return CloudStatus.OFF;
        }

        return original.call(instance);
    }
}
