package me.icanttellyou.mods.photomode.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    private void injectRenderSky(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void injectRenderClouds(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @ModifyVariable(method = "render", at = @At("HEAD"))
    private Matrix4f injectRender(Matrix4f matrix4f, MatrixStack matrices, float tickDelta) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.client.currentScreen).cameraZoom);
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            matrix4f.setOrtho(-width, width, -height, height, -9999, 9999);
        }
        return matrix4f;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0, require = 0)
    private Matrix4f injectRenderM4F(Matrix4f matrix4f) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.client.currentScreen).cameraZoom);
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            return matrix4f.setOrtho(-width, width, -height, height, -9999, 9999);
        }
        return matrix4f;
    }

    @ModifyVariable(method = "setupFrustum", at = @At("HEAD"), argsOnly = true)
    private Matrix4f injectSetupFrustum(Matrix4f matrix4f) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.client.currentScreen).cameraZoom);
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            matrix4f.setOrtho(-Math.max(10, width), Math.max(10, width), -Math.max(10, height), Math.max(10, height), -9999, 9999);
        }
        return matrix4f;
    }

    @ModifyVariable(method = "setupFrustum", at = @At("STORE"), ordinal = 0, require = 0)
    private Matrix4f injectSetupFrustumM4F(Matrix4f matrix4f) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.client.currentScreen).cameraZoom);
            float width = client.getWindow().getFramebufferWidth() / div;
            float height = client.getWindow().getFramebufferHeight() / div;
            matrix4f.setOrtho(-Math.max(10, width), Math.max(10, width), -Math.max(10, height), Math.max(10, height), -9999, 9999);
        }
        return matrix4f;
    }

}
