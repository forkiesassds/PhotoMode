package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique private PictureModeState pm$state = null;
    @Shadow @Final private Minecraft minecraft;

    //? if >=1.21.9
    @Shadow public abstract Matrix4f getProjectionMatrix(float fov);
    @Shadow public abstract float getDepthFar();

    @Inject(method = "render", at = @At("HEAD"))
    private void updatePMState(CallbackInfo ci) {
        if (pm$state == null)
            pm$state = PictureModeClient.getState();
    }

    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void replaceFOVWithDeltaTicks(
        Camera camera,
        float partialTick,
        boolean useFovSetting,
        CallbackInfoReturnable</*? >=1.21.2 {*/Float/*?} else {*//*Double*//*?}*/> cir
    ) {
        if (pm$state.isEnabled())
            cir.setReturnValue(/*? <1.21.2 {*//*(double) *//*?}*/ partialTick);
    }

    @Inject(method = "getProjectionMatrix", at = @At("HEAD"), cancellable = true)
    private void setupPMMatrices(/*? >=1.21.2 {*/ float /*?} else {*/ /*double *//*?}*/ fov, CallbackInfoReturnable<Matrix4f> cir) {
        if (!pm$state.isEnabled())
            return;

        int width = minecraft.getWindow().getWidth();
        int height = minecraft.getWindow().getHeight();

        float farPlane = getDepthFar();

        cir.setReturnValue(pm$state.getProjectionMatrix(width, height, farPlane, fov));
    }

    //? if >=1.21.9 {
    @Inject(method = "getProjectionMatrixForCulling", at = @At("HEAD"), cancellable = true)
    private void setupPMMatricesForCulling(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        if (pm$state.isEnabled())
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
        if (pm$state.isEnabled())
            return a;

        return original.call(a, b);
    }
    *///? }

    @Inject(method = {"bobView", "bobHurt"}, at = @At("HEAD"), cancellable = true)
    private void cancelBobbingInPM(CallbackInfo ci) {
        if (pm$state.isEnabled())
            ci.cancel();
    }

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void hideHandInPM(CallbackInfo ci) {
        if (pm$state.isEnabled())
            ci.cancel();
    }

    @Definition(id = "renderLevel", local = @Local(type = boolean.class, ordinal = 0, argsOnly = true))
    @Expression("renderLevel")
    @ModifyExpressionValue(method = "render", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean hideHudInPM(boolean original) {
        if (pm$state == null)
            return original;

        return !pm$state.isEnabled() && original;
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
        return pm$state.isEnabled() || original.call(instance);
    }
    //? }
}
