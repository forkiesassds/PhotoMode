package me.icanttellyou.mods.photomode.mixin;

import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @ModifyArg(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
    private static float injectApplyFog(float fog) {
        float fogModifier = 1.0f;
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            fogModifier = ((PhotoModeScreen)MinecraftClient.getInstance().currentScreen).cameraFog;
        }
        return fog * fogModifier;
    }

    @ModifyArg(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"))
    private static float injectApplyFog2(float fog) {
        float fogModifier = 1.0f;
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            fogModifier = ((PhotoModeScreen)MinecraftClient.getInstance().currentScreen).cameraFog;
        }
        return fog * fogModifier;
    }
}
