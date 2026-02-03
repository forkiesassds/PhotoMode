package mod.icanttellyou.picturemode.mixin.compat.vulkanmod;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.vulkanmod.render.chunk.buffer.DrawBuffers", remap = false)
public abstract class DrawBuffersMixin {
    @Definition(id = "CONFIG", field = "Lnet/vulkanmod/Initializer;CONFIG:Lnet/vulkanmod/config/Config;")
    @Definition(id = "backFaceCulling", field = "Lnet/vulkanmod/config/Config;backFaceCulling:Z")
    @Expression("CONFIG.backFaceCulling")
    @ModifyExpressionValue(method = "buildDrawBatchesDirect", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableBackfaceCullingInPMDirect(boolean original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original;

        return !state.isEnabled() && original;
    }

    @Definition(id = "CONFIG", field = "Lnet/vulkanmod/Initializer;CONFIG:Lnet/vulkanmod/config/Config;")
    @Definition(id = "backFaceCulling", field = "Lnet/vulkanmod/config/Config;backFaceCulling:Z")
    @Expression("CONFIG.backFaceCulling")
    @ModifyExpressionValue(method = "buildDrawBatchesIndirect", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableBackfaceCullingInPMIndirect(boolean original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original;

        return !state.isEnabled() && original;
    }
}
