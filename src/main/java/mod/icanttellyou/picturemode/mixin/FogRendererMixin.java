package mod.icanttellyou.picturemode.mixin;

//? if >=1.21.6 {
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
//? } else {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.renderer.FogRenderer;
*///? }
import net.minecraft.client.Camera;
//? if >=1.21.6
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.6 {
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? }

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    //? if >=1.21.6 {
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
    //? } else {
    /*@WrapOperation(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"
        )
    )
    private static void applyPMFogModifierStart(
        float f,
        Operation<Void> original,
        Camera camera,
        FogRenderer.FogMode fogMode,
        float farPlaneDistance,
        boolean shouldCreateFog,
        float partialTick
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled()) {
            original.call(f);
            return;
        }

        float fogModifier = (float) state.fog.getValue(partialTick);
        original.call(f * fogModifier);
    }

    @WrapOperation(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"
        )
    )
    private static void applyPMFogModifierEnd(
        float f,
        Operation<Void> original,
        Camera camera,
        FogRenderer.FogMode fogMode,
        float farPlaneDistance,
        boolean shouldCreateFog,
        float partialTick
    ) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled()) {
            original.call(f);
            return;
        }

        float fogModifier = (float) state.fog.getValue(partialTick);
        original.call(f * fogModifier);
    }
    *///? }
}