package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(/*? >=26.2 {*/ /*net.minecraft.client.renderer.extract.LevelExtractor.class *//*? } else {*/ net.minecraft.client.renderer.LevelRenderer.class /*? }*/)
public class LevelRendererMixinVanilla {
    @Unique private double pm$lastZoom = Double.MIN_VALUE;
    @Unique private double pm$lastPanX = Double.MIN_VALUE;
    @Unique private double pm$lastPanY = Double.MIN_VALUE;

    @Shadow @Final private Minecraft minecraft;

    @Definition(
        id = "prevCamRotY",
        //? if >=26.2 {
        /*field = "Lnet/minecraft/client/renderer/extract/LevelExtractor;prevCamRotY:D"
        *///? } else {
        field = "Lnet/minecraft/client/renderer/LevelRenderer;prevCamRotY:D"
        //? }
    )
    //? if >=26.1 {
    /*@Definition(id = "h", local = @Local(type = double.class, name = "camRotY"))
    *///? } else {
    @Definition(id = "h", local = @Local(type = double.class, ordinal = /*? >=1.21.9 {*/ 4 /*? } else {*/ /*7 *//*?}*/))
    //? }
    @Expression("h != this.prevCamRotY")
    @ModifyExpressionValue(
        //? if >=26.2 {
        /*method = "extract",
        *///? } else if >=1.21.9 {
        method = "cullTerrain",
        //? } else {
        /*method = "setupRender",
        *///? }
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean checkIfPMZoomChanged(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        double delta = /*? >=1.21 {*/ minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*minecraft.getFrameTime() *//*?}*/;

        return original || (state.isEnabled() && (state.cameraZoom.getValue(delta) != pm$lastZoom ||
                state.cameraPanX.getValue(delta) != pm$lastPanX || state.cameraPanY.getValue(delta) != pm$lastPanY));
    }

    @Inject(
        //? if >=26.2 {
        /*method = "extract",
        *///? } else if >=1.21.9 {
        method = "cullTerrain",
        //? } else {
        /*method = "setupRender",
        *///? }
        at = @At(
            value = "FIELD",
            //? if >=26.2 {
            /*target = "Lnet/minecraft/client/renderer/extract/LevelExtractor;prevCamRotY:D",
            *///? } else {
            target = "Lnet/minecraft/client/renderer/LevelRenderer;prevCamRotY:D",
             //? }
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

    //? if <26.2 {
    @Definition(id = "minecraft", field = "Lnet/minecraft/client/renderer/LevelRenderer;minecraft:Lnet/minecraft/client/Minecraft;")
    @Definition(id = "smartCull", field = "Lnet/minecraft/client/Minecraft;smartCull:Z")
    @Expression("this.minecraft.smartCull")
    @ModifyExpressionValue(
        //? if >=1.21.9 {
        method = "cullTerrain",
        //? } else {
        /*method = "setupRender",
        *///? }
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return (state == null || !state.isEnabled()) && original;
    }
    //? }
}
