package me.icanttellyou.mods.photomode.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.util.Handle;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(PostEffectPass.class)
public abstract class MixinPostEffectPass {
    @Inject(method = "method_67884", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;[F)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void photoMode$setupShaderUniform(
            Handle<Framebuffer> handle,
            Matrix4f matrix4f,
            Map<Identifier, Handle<Framebuffer>> map,
            Consumer<RenderPass> consumer,
            CallbackInfo ci,
            Framebuffer framebuffer,
            GpuBuffer gpuBuffer,
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer,
            GpuBuffer gpuBuffer2,
            RenderPass renderPass
    ) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen pmScreen) {
            renderPass.setUniform("Intensity", pmScreen.shaderIntensity);
        }
    }
}
