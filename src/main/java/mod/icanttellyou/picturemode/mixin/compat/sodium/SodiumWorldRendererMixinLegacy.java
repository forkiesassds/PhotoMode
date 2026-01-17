//? if <=1.21.1 {
/*package mod.icanttellyou.picturemode.mixin.compat.sodium;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer", remap = false)
public class SodiumWorldRendererMixinLegacy {
    @Unique
    private double pm$lastZoom = Double.MIN_VALUE;

    @Shadow
    private Minecraft client;

    @Definition(id = "lastFogDistance", field = "Lme/jellysquid/mods/sodium/client/render/SodiumWorldRenderer;lastFogDistance:F")
    @Definition(id = "fogDistance", local = @Local(type = float.class, ordinal = 2))
    @Expression("fogDistance != this.lastFogDistance")
    @ModifyExpressionValue(method = "setupTerrain", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkIfPMZoomChanged(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        double delta = /^? >=1.21 {^/ client.getDeltaTracker().getGameTimeDeltaPartialTick(true) /^?} else {^/ /^client.getFrameTime() ^//^?}^/;

        return original || (state.isEnabled() && state.cameraZoom.getValue(delta) != pm$lastZoom);
    }

    @Inject(
        method = "setupTerrain",
        at = @At(
            value = "FIELD",
            target = "Lme/jellysquid/mods/sodium/client/render/SodiumWorldRenderer;lastFogDistance:F",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD
        )
    )
    private void storeLastPMZoom(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled())
            return;

        double delta = /^? >=1.21 {^/ client.getDeltaTracker().getGameTimeDeltaPartialTick(true) /^?} else {^/ /^client.getFrameTime() ^//^?}^/;
        this.pm$lastZoom = state.cameraZoom.getValue(delta);
    }
}
*///? }