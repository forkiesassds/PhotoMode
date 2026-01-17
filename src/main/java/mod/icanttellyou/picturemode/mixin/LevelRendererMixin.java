package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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

        if (state.isEnabled())
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

        if (state.isEnabled())
            return CloudStatus.OFF;

        return original.call(instance);
    }

    //? if <1.21.9 {
    /*@Definition(id = "isSpectator", local = @Local(type = boolean.class, ordinal = 1, argsOnly = true))
    @Expression("isSpectator")
    @ModifyExpressionValue(method = "setupRender", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return !state.isEnabled() && original;
    }
    *///? }
}
