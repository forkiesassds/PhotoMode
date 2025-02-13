package me.icanttellyou.mods.photomode.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 999)
public abstract class MixinWorldRendererVanilla {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Unique
    private double lastCameraZoom = Double.MIN_VALUE;

    @ModifyExpressionValue(
            method = "setupTerrain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ChunkRenderingDataPreparer;method_52836()Z")
    )
    private boolean photoMode$shouldUpdateFrustum(boolean original) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            double zoom = ((PhotoModeScreen) client.currentScreen).getZoom(client.getRenderTickCounter().getTickDelta(true));
            return original || lastCameraZoom != zoom;
        } else return original;
    }

    @Inject(
            method = "setupTerrain",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/ChunkRenderingDataPreparer;method_52836()Z",
                    shift = At.Shift.AFTER
            )
    )
    private void photoMode$updateLastZoom(CallbackInfo ci) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            lastCameraZoom = ((PhotoModeScreen) client.currentScreen).getZoom(client.getRenderTickCounter().getTickDelta(true));
        }
    }
}
