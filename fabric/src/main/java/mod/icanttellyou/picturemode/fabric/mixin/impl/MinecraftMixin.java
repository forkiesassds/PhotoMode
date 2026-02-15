package mod.icanttellyou.picturemode.fabric.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderTarget;
import mod.icanttellyou.picturemode.fabric.client.event.OnGameRenderEvents;
import mod.icanttellyou.picturemode.fabric.client.event.RenderTargetBlitEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @WrapOperation(
        method = "runTick",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(" +
                //? if <1.21.5
                //"II" +
                ")V"
        )
    )
    private void onScreenBlit(RenderTarget instance, /*? <1.21.5 {*/ /*int width, int height, *//*?}*/ Operation<Void> original) {
        if (!RenderTargetBlitEvents.BEFORE.invoker().beforeTargetBlit(instance))
            return;

        original.call(instance /*? <1.21.5 {*/ /*, width, height *//*?}*/);
        RenderTargetBlitEvents.AFTER.invoker().afterTargetBlit(instance);
    }

    @WrapOperation(
        method = "runTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;render(" +
                //? if >=1.21 {
                "Lnet/minecraft/client/DeltaTracker;"
                //? } else {
                /*"FJ"
                *///? }
                + "Z)V"
        )
    )
    private void onGameRender(
        GameRenderer instance,
        //? if >=1.21 {
        net.minecraft.client.DeltaTracker tracker,
        //? } else {
        /*float partialTicks,
        long nanoTime,
        *///? }
        boolean renderLevel,
        Operation<Void> original
    ) {
        OnGameRenderEvents.BEFORE.invoker().beforeGameRender(instance, renderLevel);
        original.call(instance, /*? >=1.21 {*/ tracker, /*?} else {*/ /*partialTicks, nanoTime, *//*?}*/ renderLevel);
        OnGameRenderEvents.AFTER.invoker().afterGameRender(instance, renderLevel);
    }
}
