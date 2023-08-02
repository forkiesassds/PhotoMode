package me.icanttellyou.mods.photomode.common.mixin.compat;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSectionManager.class)
public class MixinRenderSectionManager {
    @Inject(method = "getVisibleFaces", at = @At("HEAD"), remap = false, cancellable = true)
    private void photoMode$sodium05x$getVisibleFaces(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen)
            cir.setReturnValue(ModelQuadFacing.ALL);
    }
}
