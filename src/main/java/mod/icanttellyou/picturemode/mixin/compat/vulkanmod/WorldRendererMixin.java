package mod.icanttellyou.picturemode.mixin.compat.vulkanmod;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "net.vulkanmod.render.chunk.WorldRenderer", remap = false)
public class WorldRendererMixin {
    @Unique private double pm$lastZoom = Double.MIN_VALUE;
    @Unique private double pm$lastPanX = Double.MIN_VALUE;
    @Unique private double pm$lastPanY = Double.MIN_VALUE;

    @Shadow @Final private Minecraft minecraft;

    @Definition(id = "d_yRot", local = @Local(type = float.class, ordinal = 4))
    @Expression("d_yRot > 2.0")
    @ModifyExpressionValue(method = "setupRenderer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkIfPMZoomChanged(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        double delta = /*? >=1.21 {*/ minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*minecraft.getFrameTime() *//*?}*/;

        return original || (state.isEnabled() && (state.cameraZoom.getValue(delta) != pm$lastZoom ||
                state.cameraPanX.getValue(delta) != pm$lastPanX || state.cameraPanY.getValue(delta) != pm$lastPanY));
    }

    @Inject(
        method = "setupRenderer",
        at = @At(
            value = "FIELD",
            target = "Lnet/vulkanmod/render/chunk/WorldRenderer;lastCamRotY:F",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD
        )
    )
    private void storeLastPMZoom(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        double delta = /*? >=1.21 {*/ minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*minecraft.getFrameTime() *//*?}*/;
        this.pm$lastZoom = state.cameraZoom.getValue(delta);
        this.pm$lastPanX = state.cameraPanX.getValue(delta);
        this.pm$lastPanY = state.cameraPanY.getValue(delta);
    }
}
