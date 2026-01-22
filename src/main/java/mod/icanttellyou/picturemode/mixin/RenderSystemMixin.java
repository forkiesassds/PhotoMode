//? if <=1.21.6 {
/*package mod.icanttellyou.picturemode.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @ModifyVariable(method = "setShaderFogStart", at = @At(value = "HEAD"), argsOnly = true)
    private static float applyPMFogModifierStart(float f) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled() || f == Float.MAX_VALUE) {
            return f;
        }

        Minecraft mc = Minecraft.getInstance();
        double delta = /^? >=1.21 {^/ mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) /^?} else {^/ /^mc.getFrameTime() ^//^?}^/;
        float fogModifier = (float) state.fog.getValue(delta);
        return f * fogModifier;
    }

    @ModifyVariable(method = "setShaderFogEnd", at = @At(value = "HEAD"), argsOnly = true)
    private static float applyPMFogModifierEnd(float f) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled()) {
            return f;
        }

        Minecraft mc = Minecraft.getInstance();
        double delta = /^? >=1.21 {^/ mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) /^?} else {^/ /^mc.getFrameTime() ^//^?}^/;
        float fogModifier = (float) state.fog.getValue(delta);
        return f * fogModifier;
    }
}
*///? }
