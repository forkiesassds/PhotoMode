package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void injectBobView(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void injectRenderHand(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) info.cancel();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private void photoMode$forceHideHud(InGameHud instance, MatrixStack stack, float tickDelta) {
        if (!(client.currentScreen instanceof PhotoModeScreen)) instance.render(stack, tickDelta);
    }
}
