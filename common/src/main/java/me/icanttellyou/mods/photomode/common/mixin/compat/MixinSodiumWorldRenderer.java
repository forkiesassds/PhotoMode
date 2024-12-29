package me.icanttellyou.mods.photomode.common.mixin.compat;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SodiumWorldRenderer.class)
public abstract class MixinSodiumWorldRenderer {
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Unique
    private double lastCameraZoom = Double.MIN_VALUE;

    @ModifyVariable(method = "setupTerrain", at = @At(value = "LOAD"), ordinal = 2, remap = false)
    private boolean photoMode$hackDirtyFlag(boolean dirty) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            double zoom = ((PhotoModeScreen) client.currentScreen).getZoom(client.getRenderTickCounter().getTickDelta(true));
            if (lastCameraZoom != zoom) {
                lastCameraZoom = zoom;
                return true;
            }
        }

        return dirty;
    }
}
