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

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void injectBobView(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void injectRenderHand(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }
    @Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"), cancellable = true)
    private void injectGetBasicProjectionMatrix(CallbackInfoReturnable<Matrix4f> cir) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.client.currentScreen).cameraZoom);
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            cir.setReturnValue(matrixStack.peek().getPositionMatrix().mul((new Matrix4f()).setOrtho(-width, width, -height, height, ((GameRenderer)(Object)this).method_32796() * -2.0f, ((GameRenderer)(Object)this).method_32796() * 2.0f)));
        }
    }
}
