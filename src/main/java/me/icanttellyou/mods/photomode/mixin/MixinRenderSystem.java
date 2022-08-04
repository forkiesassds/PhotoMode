package me.icanttellyou.mods.photomode.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
    @ModifyVariable(method = "setShaderFogStart", at = @At(value = "HEAD"), argsOnly = true)
    private static float injectSetShaderFogStart(float value) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen && value != Float.MAX_VALUE) {
            float fogModifier = ((PhotoModeScreen)MinecraftClient.getInstance().currentScreen).cameraFog;
            return value * fogModifier;
        }
        return value;
    }

    @ModifyVariable(method = "setShaderFogEnd", at = @At(value = "HEAD"), argsOnly = true)
    private static float injectSetShaderFogEnd(float value) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            float fogModifier = ((PhotoModeScreen)MinecraftClient.getInstance().currentScreen).cameraFog;
            return value * fogModifier;
        }
        return value;
    }
}
