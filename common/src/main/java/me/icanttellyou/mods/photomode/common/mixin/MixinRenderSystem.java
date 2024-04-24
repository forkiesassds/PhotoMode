package me.icanttellyou.mods.photomode.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {
    @ModifyVariable(method = "setShaderFogStart", at = @At(value = "HEAD"), argsOnly = true)
    private static float injectSetShaderFogStart(float value) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen instanceof PhotoModeScreen && value != Float.MAX_VALUE) {
            float fogModifier = ((PhotoModeScreen) client.currentScreen).getFog(client.getTickDelta());
            return value * fogModifier;
        }
        return value;
    }

    @ModifyVariable(method = "setShaderFogEnd", at = @At(value = "HEAD"), argsOnly = true)
    private static float injectSetShaderFogEnd(float value) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen instanceof PhotoModeScreen) {
            float fogModifier = ((PhotoModeScreen) client.currentScreen).getFog(client.getTickDelta());
            return value * fogModifier;
        }
        return value;
    }
}
