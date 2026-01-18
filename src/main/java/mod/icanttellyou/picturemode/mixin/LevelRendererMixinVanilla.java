package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixinVanilla {
    @Unique private double pm$lastZoom = Double.MIN_VALUE;

    @Shadow @Final private Minecraft minecraft;

    @Definition(id = "prevCamRotY", field = "Lnet/minecraft/client/renderer/LevelRenderer;prevCamRotY:D")
    //? if >=1.21.9 {
    @Definition(id = "h", local = @Local(type = double.class, ordinal = 4))
    @Expression("h != this.prevCamRotY")
    @ModifyExpressionValue(method = "cullTerrain", at = @At("MIXINEXTRAS:EXPRESSION"))
    //? } else {
    /*@Definition(id = "n", local = @Local(type = double.class, ordinal = 7))
    @Expression("n != this.prevCamRotY")
    @ModifyExpressionValue(method = "setupRender", at = @At("MIXINEXTRAS:EXPRESSION"))
    *///? }
    private boolean checkIfPMZoomChanged(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        double delta = /*? >=1.21 {*/ minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*minecraft.getFrameTime() *//*?}*/;

        return original || (state.isEnabled() && state.cameraZoom.getValue(delta) != pm$lastZoom);
    }

    @Inject(
        //? if >=1.21.9 {
        method = "cullTerrain",
        //? } else {
        /*method = "setupRender",
        *///? }
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;prevCamRotY:D",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD
        )
    )
    private void storeLastPMZoom(CallbackInfo ci) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled())
            return;

        double delta = /*? >=1.21 {*/ minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*minecraft.getFrameTime() *//*?}*/;
        this.pm$lastZoom = state.cameraZoom.getValue(delta);
    }

    @Definition(id = "minecraft", field = "Lnet/minecraft/client/renderer/LevelRenderer;minecraft:Lnet/minecraft/client/Minecraft;")
    @Definition(id = "smartCull", field = "Lnet/minecraft/client/Minecraft;smartCull:Z")
    @Expression("this.minecraft.smartCull")
    @ModifyExpressionValue(method = /*? >=1.21.9 {*/ "cullTerrain" /*?} else {*/ /*"setupRender" *//*?}*/, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return state.isEnabled() && original;
    }
}
