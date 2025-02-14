package me.icanttellyou.mods.photomode.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.util.Handle;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PostEffectPass.class)
public abstract class MixinPostEffectPass {
    @Inject(method = "method_67884", at = @At("HEAD"))
    private void photoMode$fixFabulousGraphics$1(Handle<Framebuffer> handle, Matrix4f matrix4f, Map<Identifier, Handle<Framebuffer>> map, CallbackInfo ci) {
        RenderSystem.depthMask(false);
    }

    @Inject(method = "method_67884", at = @At("TAIL"))
    private void photoMode$fixFabulousGraphics$2(Handle<Framebuffer> handle, Matrix4f matrix4f, Map<Identifier, Handle<Framebuffer>> map, CallbackInfo ci) {
        RenderSystem.depthMask(true);
    }

    @Inject(method = "method_67885", at = @At("HEAD"))
    private void photoMode$setupShaderUniform(Map<Identifier, Handle<Framebuffer>> map, Framebuffer framebuffer, ShaderProgram shaderProgram, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen pmScreen) {
            shaderProgram.getUniformOrDefault("Intensity").set(pmScreen.shaderIntensity);
        }
    }
}
