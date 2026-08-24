//? if >=26.2 {
/*package mod.icanttellyou.picturemode.mixin.impl;

import com.mojang.blaze3d.opengl.GlSurface;
import mod.icanttellyou.picturemode.imixin.PMSkippableGpuSurface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlSurface.class)
public abstract class GlSurfaceMixin implements PMSkippableGpuSurface {
    @Unique private boolean pm$skippedFrame;

    @Override
    public void pm$skipFrame() {
        this.pm$skippedFrame = true;
    }

    @Inject(method = "blitFromTexture", at = @At("HEAD"), cancellable = true)
    private void skipBlit(CallbackInfo ci) {
        if (!this.pm$skippedFrame)
            return;

        ci.cancel();
    }

    @Inject(method = "present", at = @At("HEAD"), cancellable = true)
    private void skipPresent(CallbackInfo ci) {
        if (!this.pm$skippedFrame)
            return;

        this.pm$skippedFrame = false;
        ci.cancel();
    }
}
*///? }