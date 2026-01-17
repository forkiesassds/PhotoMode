package mod.icanttellyou.picturemode.mixin.compat.sodium;

//? if <=1.21.1 {
/*import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = {
    //? if >=1.21
    "net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer"
    /*? if >=1.21 && <=1.21.1 {*//*, *//*? }*/
    //? if <=1.21.1
    //"me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer"
}, remap = false)
public abstract class SodiumWorldRendererMixin {
    //? if <=1.21.1 {
    /*@Unique private double pm$lastZoom = Double.MIN_VALUE;

    @Shadow private Minecraft client;

    @Definition(id = "dirty", local = @Local(type = boolean.class, ordinal = 0))
    @Expression("dirty")
    @ModifyExpressionValue(method = "setupTerrain", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean checkIfPMZoomChanged(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        double delta = /^? >=1.21 {^/ client.getDeltaTracker().getGameTimeDeltaPartialTick(true) /^?} else {^/ /^client.getFrameTime() ^//^?}^/;

        return original || (state.isEnabled() && state.cameraZoom.getValue(delta) != pm$lastZoom);
    }

    @Inject(
        method = "setupRender",
        at = @At(
            value = "FIELD",
            target = "Lme/jellysquid/mods/sodium/client/render/SodiumWorldRenderer;lastFogDistance:D",
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
    *///? }

    @ModifyVariable(method = "setupTerrain", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return !state.isEnabled() && original;
    }
}