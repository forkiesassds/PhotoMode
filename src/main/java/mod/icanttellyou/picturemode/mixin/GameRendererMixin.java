package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <26.1 {
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? }

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    //? if <26.1 {
    //? if >=1.21.9
    @Shadow public abstract Matrix4f getProjectionMatrix(float fov);
    @Shadow public abstract float getDepthFar();

    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void replaceFOVWithDeltaTicks(
        Camera camera,
        float partialTick,
        boolean useFovSetting,
        CallbackInfoReturnable</*? >=1.21.2 {*/Float/*?} else {*//*Double*//*?}*/> cir
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            cir.setReturnValue(/*? <1.21.2 {*//*(double) *//*?}*/ partialTick);
    }

    @Inject(method = "getProjectionMatrix", at = @At("HEAD"), cancellable = true)
    private void setupPMMatrices(/*? >=1.21.2 {*/ float /*?} else {*/ /*double *//*?}*/ fov, CallbackInfoReturnable<Matrix4f> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        int width = PictureModeClient.getWidth();
        int height = PictureModeClient.getHeight();

        float farPlane = getDepthFar();

        cir.setReturnValue(state.getProjectionMatrix(width, height, farPlane, fov));
    }

    //? if >=1.21.9 {
    @Inject(method = "getProjectionMatrixForCulling", at = @At("HEAD"), cancellable = true)
    private void setupPMMatricesForCulling(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            cir.setReturnValue(getProjectionMatrix(fov));
    }
    //? } else {
    /*//? if >=1.21.2 {
    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", remap = false))
    private float bypassFOVComparisonInPM(float a, float b, Operation<Float> original) {
    //? } else {
    /^@WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D", remap = false))
    private double bypassFOVComparisonInPM(double a, double b, Operation<Double> original) {
    ^///? }
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            return a;

        return original.call(a, b);
    }
    *///? }
    //? }

    @Inject(method = {"bobView", "bobHurt"}, at = @At("HEAD"), cancellable = true)
    private void cancelBobbingInPM(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }

    @WrapOperation(
        method = "renderLevel",
        //? if >=1.21.6 {
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V"
        )
        //? } else {
        /*at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V"
        )
        *///? }
    )
    private void doNotClearDepthTextureInPM(
        //? if >=1.21.6 {
        com.mojang.blaze3d.systems.CommandEncoder o,
        com.mojang.blaze3d.textures.GpuTexture o2,
        double v,
        //? } else {
        /*int o,
        boolean o2,
        *///? }
        Operation<Void> original
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            original.call(o, o2 /*? >=1.21.6 {*/, v/*?}*/);
    }

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void hideHandInPM(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            ci.cancel();
    }

    @Definition(id = "renderLevel", local = @Local(type = boolean.class, ordinal = 0, argsOnly = true))
    @Expression("renderLevel")
    @ModifyExpressionValue(
        //? if >=26.1 {
        /*method = "extractGui",
        *///? } else {
        method = "render",
        //? }
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            ordinal = /*? >=26.1 {*/ /*0 *//*? } else {*/ 1 /*? }*/
        )
    )
    private boolean hideHudInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original;

        return !state.isEnabled() && original;
    }

    @WrapOperation(
        //? if >=26.1 {
        /*method = "extractGui",
        *///? } else {
        method = "render",
        //? }
        at = @At(
            value = "INVOKE",
            //? if >=1.21.2 {
            target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            //? } else {
            /*target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            *///? }
        )
    )
    private void hideToastsInPM(@Coerce Object instance, GuiGraphics i, Operation<Void> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            original.call(instance, i);
    }

    //? if >=1.21.6 {
    @WrapOperation(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;shouldCreateWorldFog()Z"
        )
    )
    private boolean hideSkyInPM(net.minecraft.client.gui.components.BossHealthOverlay instance, Operation<Boolean> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original.call(instance);

        return state.isEnabled() || original.call(instance);
    }
    //? }

    //? if >=26.1 {
    /*@WrapOperation(
        method = "extractOptions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;getCloudStatus()Lnet/minecraft/client/CloudStatus;"
        )
    )
    private net.minecraft.client.CloudStatus hideCloudsInPM(
        net.minecraft.client.Options instance,
        Operation<net.minecraft.client.CloudStatus> original
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled()) {
            return net.minecraft.client.CloudStatus.OFF;
        }

        return original.call(instance);
    }
    *///? }
}
