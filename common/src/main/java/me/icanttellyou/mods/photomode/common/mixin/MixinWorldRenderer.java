package me.icanttellyou.mods.photomode.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void injectRenderSky(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void injectRenderClouds(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    /**
     * Fixes depth buffer being messed up in fabulous graphics... Somewhat, as depth information is not present for translucent surfaces.
     */
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/PostEffectProcessor;method_62234(Lnet/minecraft/client/render/FrameGraphBuilder;IILnet/minecraft/client/gl/PostEffectProcessor$FramebufferSet;)V",
                    ordinal = 1
            )
    )
    private void photoMode$fixFabulousDepthSomewhat(PostEffectProcessor instance, FrameGraphBuilder frameGraphBuilder, int i, int j, PostEffectProcessor.FramebufferSet framebufferSet) {
        RenderSystem.depthMask(false);
        instance.method_62234(frameGraphBuilder, i, j, framebufferSet);
        RenderSystem.depthMask(true);
    }
}
