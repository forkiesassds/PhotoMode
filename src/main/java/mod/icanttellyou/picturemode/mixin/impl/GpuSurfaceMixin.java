//? if >=26.2 {
/*package mod.icanttellyou.picturemode.mixin.impl;

import com.mojang.blaze3d.systems.CommandEncoder;
//? if <26.3
import com.mojang.blaze3d.systems.GpuSurface;
import com.mojang.blaze3d.systems.GpuSurfaceBackend;
import com.mojang.blaze3d.textures.GpuTextureView;
//? if >=26.3 {
/^import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import com.mojang.renderpearl.frontend.FrontendGpuSurface;
^///? }
import mod.icanttellyou.picturemode.imixin.PMSkippableGpuSurface;
//? if <26.3
import mod.icanttellyou.picturemode.mixin.CommandEncoderAccessor;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//~ if >=26.3 'GpuSurface' -> 'FrontendGpuSurface'
@Mixin(GpuSurface.class)
public abstract class GpuSurfaceMixin implements PMSkippableGpuSurface {
    @Shadow @Final private GpuSurfaceBackend backend;
    @Shadow private boolean hasImageAcquired;
    @Shadow private boolean hasBlittedTexture;

    @Unique private boolean pm$skippedFrame;
    @Unique private boolean pm$needsSpecialInstructions;

    @Override
    public void pm$skipFrame() {
        this.pm$skippedFrame = true;

        if (this.pm$needsSpecialInstructions)
            ((PMSkippableGpuSurface) this.backend).pm$skipFrame();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onSurfaceInit(GpuSurfaceBackend backend, CallbackInfo ci) {
        if (backend instanceof PMSkippableGpuSurface) {
            this.pm$needsSpecialInstructions = true;
        } else {
            LoggingUtil.log(Level.INFO, "Graphics backend does not have " +
                    "proper instructions for skipping frames. There may be issues.");
        }
    }

    //? if >=26.3
    //@SuppressWarnings("UnstableApiUsage")
    @Inject(method = "blitFromTexture", at = @At("HEAD"), cancellable = true)
    private void skipBlit(CommandEncoder commandEncoder, GpuTextureView textureView, CallbackInfo ci) {
        if (!this.pm$skippedFrame)
            return;

        if (this.pm$needsSpecialInstructions)
            //~ if >=26.3 'CommandEncoderAccessor' -> 'FrontendCommandEncoder', 'invokeBackend()' -> 'backend()'
            this.backend.blitFromTexture(((CommandEncoderAccessor) commandEncoder).invokeBackend(), textureView);
        this.hasBlittedTexture = true;

        ci.cancel();
    }

    @Inject(method = "present", at = @At("HEAD"), cancellable = true)
    private void skipPresent(CallbackInfo ci) {
        if (!this.pm$skippedFrame)
            return;

        if (this.pm$needsSpecialInstructions)
            this.backend.present();
        this.hasImageAcquired = false;
        this.pm$skippedFrame = false;

        ci.cancel();
    }
}
*///? }
