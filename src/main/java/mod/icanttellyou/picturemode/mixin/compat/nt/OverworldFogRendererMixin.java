package mod.icanttellyou.picturemode.mixin.compat.nt;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "mod.adrenix.nostalgic.helper.candy.level.fog.OverworldFogRenderer", remap = false)
public abstract class OverworldFogRendererMixin {
    @WrapOperation(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
            remap = false
        )
    )
    private static void modifyNTFogInPM(Consumer<?> instance, Object value, Operation<Void> original) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled()) {
            original.call(instance, value);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        double delta = /*? >=1.21 {*/ mc.getDeltaTracker().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*mc.getFrameTime() *//*?}*/;
        float fogModifier = (float) state.fog.getValue(delta);
        original.call(instance, (float) value * fogModifier);
    }
}
