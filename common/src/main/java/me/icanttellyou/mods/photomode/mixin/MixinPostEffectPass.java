package me.icanttellyou.mods.photomode.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.util.Handle;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PostEffectPass.class)
public abstract class MixinPostEffectPass {
    @Shadow @Final private ShaderProgram program;

    @Inject(method = "method_62257", at = @At("HEAD"))
    private void photoMode$doThingsWithPass$1(Handle<Framebuffer> handle, Map<Identifier, Handle<Framebuffer>> map, Matrix4f matrix4f, CallbackInfo ci) {
        RenderSystem.depthMask(false);

        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen pmScreen) {
            program.getUniformOrDefault("Intensity").set(pmScreen.shaderIntensity);
        }
    }

    @Inject(method = "method_62257", at = @At("TAIL"))
    private void photoMode$doThingsWithPass$2(Handle<Framebuffer> handle, Map<Identifier, Handle<Framebuffer>> map, Matrix4f matrix4f, CallbackInfo ci) {
        RenderSystem.depthMask(true);
    }
}
