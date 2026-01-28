package mod.icanttellyou.picturemode.mixin.compat.vulkanmod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "net.vulkanmod.render.chunk.build.renderer.BlockRenderer", remap = false)
public class BlockRendererMixin {
    @WrapOperation(
        method = "bufferQuad",
        at = @At(
            value = "FIELD",
            target = "Lnet/vulkanmod/render/chunk/build/renderer/BlockRenderer;backFaceCulling:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean disableBackfaceCullingInPM(@Coerce Object instance, Operation<Boolean> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original.call(instance);

        return !state.isEnabled() && original.call(instance);
    }
}
