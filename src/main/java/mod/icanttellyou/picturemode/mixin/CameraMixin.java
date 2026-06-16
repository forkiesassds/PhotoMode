package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private boolean detached;

    @Shadow protected abstract void setRotation(float yRot, float xRot);

    //? if >=26.1 {
    /*@Shadow private float depthFar;
    @Shadow @org.spongepowered.asm.mixin.Final private net.minecraft.client.renderer.Projection projection;
    @Shadow @org.spongepowered.asm.mixin.Final private net.minecraft.client.Minecraft minecraft;
    @Shadow protected abstract void setupOrtho(float zNear, float zFar, float width, float height, boolean invertY);
    *///? }

    @Inject(
        //? if >=26.1 {
        /*method = "alignWithEntity",
        *///? } else {
        method = "setup",
        //? }
        at = @At("TAIL")
    )
    private void setupPMRotation(
        //? if <26.1 {
        @org.spongepowered.asm.mixin.injection.Coerce net.minecraft.world.level.BlockGetter level,
        net.minecraft.world.entity.Entity entity,
        boolean detached,
        boolean mirror,
        //? }
        float delta,
        CallbackInfo ci
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        state.setupCameraAngles(delta, this::setRotation);
        this.detached = state.isPlayerShown();
    }

    //? if >=26.1 {
    /*@Inject(method = "createProjectionMatrixForCulling", at = @At("HEAD"), cancellable = true)
    private void setupPMCullingMatrix(org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<org.joml.Matrix4f> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        float delta = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);

        int width = PictureModeClient.getWidth();
        int height = PictureModeClient.getHeight();

        cir.setReturnValue(state.getProjectionMatrix(width, height, this.depthFar, delta));
    }

    @com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Camera;setupPerspective(FFFFF)V"
        )
    )
    private void setupPMProjectionMatrix(
        Camera instance,
        float v,
        float v2,
        float v3,
        float v4,
        float v5,
        com.llamalad7.mixinextras.injector.wrapoperation.Operation<Void> original,
        net.minecraft.client.DeltaTracker tracker
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled()) {
            original.call(instance, v, v2, v3, v4, v5);
            ((mod.icanttellyou.picturemode.imixin.PMPannableProjection) this.projection)
                    .pictureMode$setPan(0, 0);
            return;
        }

        float delta = tracker.getGameTimeDeltaPartialTick(true);

        float viewWidth = (float) state.adjustViewportDimension(PictureModeClient.getWidth(), delta);
        float viewHeight = (float) state.adjustViewportDimension(PictureModeClient.getHeight(), delta);

        float panX = (float) state.cameraPanX.getValue(delta);
        float panY = (float) state.cameraPanY.getValue(delta);

        this.setupOrtho(-this.depthFar * 2, this.depthFar * 2, viewWidth * 2, viewHeight * 2, false);
        ((mod.icanttellyou.picturemode.imixin.PMPannableProjection) this.projection)
                .pictureMode$setPan(viewWidth + panX, -viewHeight + panY);
    }
    *///? }

    //? if >=26.2 {
    /*@com.llamalad7.mixinextras.expression.Definition(id = "minecraft", field = "Lnet/minecraft/client/Camera;minecraft:Lnet/minecraft/client/Minecraft;")
    @com.llamalad7.mixinextras.expression.Definition(id = "smartCull", field = "Lnet/minecraft/client/Minecraft;smartCull:Z")
    @com.llamalad7.mixinextras.expression.Expression("this.minecraft.smartCull")
    @com.llamalad7.mixinextras.injector.ModifyExpressionValue(method = "extractRenderState", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return (state == null || !state.isEnabled()) && original;
    }
    *///? }
}
