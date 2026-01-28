package mod.icanttellyou.picturemode.mixin.compat.vulkanmod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.vulkanmod.render.chunk.graph.SectionGraph", remap = false)
public abstract class SectionGraphMixin {
    @WrapOperation(
        method = "update",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;smartCull:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean disableSmartCullInPM(Minecraft instance, Operation<Boolean> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original.call(instance);

        return !state.isEnabled() && original.call(instance);
    }
}
