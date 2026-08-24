//? if >=26.2 {
/*package mod.icanttellyou.picturemode.mixin.impl;

import com.mojang.blaze3d.systems.CommandEncoderBackend;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vulkan.VulkanCommandEncoder;
import com.mojang.blaze3d.vulkan.VulkanGpuSurface;
import mod.icanttellyou.picturemode.imixin.PMSkippableGpuSurface;
import org.lwjgl.vulkan.VK13;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VulkanGpuSurface.class)
public abstract class VulkanGpuSurfaceMixin implements PMSkippableGpuSurface {
    @Shadow @Final private long[] acquireSemaphores;
    @Shadow private int currentAcquireSemaphore;

    @Unique private boolean pm$skippedFrame;

    @Override
    public void pm$skipFrame() {
        this.pm$skippedFrame = true;
    }

    @Inject(method = "blitFromTexture", at = @At("HEAD"), cancellable = true)
    private void skipBlit(CommandEncoderBackend commandEncoder, GpuTextureView textureView, CallbackInfo ci) {
        if (!this.pm$skippedFrame)
            return;

        VulkanCommandEncoder vulkanCommandEncoder = (VulkanCommandEncoder) commandEncoder;

        //Use up the semaphore to hopefully avoid a VK_TIMEOUT error on certain GPUs
        vulkanCommandEncoder.waitSemaphore(this.acquireSemaphores[this.currentAcquireSemaphore],
                0L, VK13.VK_PIPELINE_STAGE_2_ALL_COMMANDS_BIT);

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
