//? if >=1.21.6 {
package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.Camera;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Inject(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V",
            shift = At.Shift.AFTER
        )
    )
    private void applyPMFogModifier(
        Camera camera,
        int renderDistanceInChunks,
        DeltaTracker deltaTracker,
        float darkenWorldAmount,
        ClientLevel level,
        CallbackInfoReturnable<Vector4f> cir,
        @Local FogData fogData,
        @Local FogEnvironment fogEnvironment
    ) {
        if (fogEnvironment instanceof PMModifiableFog modifiableFog) {
            modifiableFog.pictureMode$modifyFog(fogData, deltaTracker);
        }
    }
}
//? }