package mod.icanttellyou.picturemode.forgelike.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.forgelike.client.event.RenderTargetBlitEvent;
import net.minecraft.client.Minecraft;
//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
//? } else {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
*///? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    //? if >=26.2
    //@org.spongepowered.asm.mixin.Unique private boolean pm$blitCancelled = false;

    @WrapOperation(
        //? if >=26.1 {
        /*method = "renderFrame",
        *///? } else {
        method = "runTick",
        //? }
        at = @At(
            value = "INVOKE",
            //? if >=26.2 {
            /*target = "Lcom/mojang/blaze3d/systems/GpuSurface;blitFromTexture(Lcom/mojang/blaze3d/systems/CommandEncoder;Lcom/mojang/blaze3d/textures/GpuTextureView;)V"
            *///? } else {
            target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(" +
                //? if <1.21.5
                //"II" +
                ")V"
            //? }
        )
    )
    private void onScreenBlit(
        //? if >=26.2 {
        /*com.mojang.blaze3d.systems.GpuSurface instance,
        com.mojang.blaze3d.systems.CommandEncoder commandEncoder,
        com.mojang.blaze3d.textures.GpuTextureView textureView,
        Operation<Void> original
        *///? } else {
        com.mojang.blaze3d.pipeline.RenderTarget instance, /*? <1.21.5 {*/ /*int width, int height, *//*?}*/ Operation<Void> original
        //? }
    ) {
        //? if neoforge {
        IEventBus bus = NeoForge.EVENT_BUS;
        //? } else {
        /*IEventBus bus = MinecraftForge.EVENT_BUS;
        *///? }
        RenderTargetBlitEvent.Pre preEvent = new RenderTargetBlitEvent.Pre(instance /*? >=26.2 {*//*, commandEncoder, textureView *//*? }*/);
        bus.post(preEvent);
        
        if (preEvent.isCanceled()) {
            //? if >=26.2
            //pm$blitCancelled = true;
            return;
        }

        //? if >=26.2 {
        /*original.call(instance, commandEncoder, textureView);
        *///? } else {
        original.call(instance /*? <1.21.5 {*/ /*, width, height *//*?}*/);
         //? }
        bus.post(new RenderTargetBlitEvent.Post(instance /*? >=26.2 {*//*, commandEncoder, textureView *//*? }*/));
    }

    //? if >=26.2 {
    /*@WrapOperation(
        method = "renderFrame",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/GpuSurface;present()V"
        )
    )
    private void onScreenPresent(com.mojang.blaze3d.systems.GpuSurface instance, Operation<Void> original) {
        if (pm$blitCancelled) {
            pm$blitCancelled = false;
            ((mod.icanttellyou.picturemode.mixin.GpuSurfaceAccessor) instance).setHasImageAcquired(false);
            return;
        }

        original.call(instance);
    }
    *///? }
}
