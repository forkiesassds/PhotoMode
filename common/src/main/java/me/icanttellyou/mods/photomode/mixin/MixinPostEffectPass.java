package me.icanttellyou.mods.photomode.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.class_10883;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuBuffer;
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
    @Inject(method = "method_67884", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_10883;method_68416(Ljava/lang/String;[F)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void photoMode$setupShaderUniform(
            Handle<Framebuffer> handle,
            Matrix4f matrix4f,
            Map<Identifier, Handle<Framebuffer>> map,
            Consumer<class_10883> consumer,
            CallbackInfo ci,
            Framebuffer framebuffer,
            GpuBuffer gpuBuffer,
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer,
            class_10883 lv
    ) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen pmScreen) {
            lv.method_68416("Intensity", pmScreen.shaderIntensity);
        }
    }
}
