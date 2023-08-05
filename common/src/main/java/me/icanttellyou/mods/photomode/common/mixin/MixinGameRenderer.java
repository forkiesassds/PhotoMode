package me.icanttellyou.mods.photomode.common.mixin;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    MinecraftClient client = MinecraftClient.getInstance();
    float delta = 0.0F;

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void injectBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        delta = tickDelta;
        if (client.currentScreen instanceof PhotoModeScreen) ci.cancel();
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void injectRenderHand(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }
    @Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"), cancellable = true)
    private void injectGetBasicProjectionMatrix(CallbackInfoReturnable<Matrix4f> cir) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) client.currentScreen).getZoom(delta));
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            cir.setReturnValue(matrixStack.peek().getPositionMatrix().mul((new Matrix4f())
                    .setOrtho(-width, width, -height, height, ((GameRenderer)(Object)this).getFarPlaneDistance() * -2.0f, ((GameRenderer)(Object)this).getFarPlaneDistance() * 2.0f)
                    .translate(((PhotoModeScreen) client.currentScreen).getPanX(delta), -((PhotoModeScreen) client.currentScreen).getPanY(delta), 0.0F)));
        }
    }
}
