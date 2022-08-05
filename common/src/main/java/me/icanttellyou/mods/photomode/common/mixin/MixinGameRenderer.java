package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;

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
}
