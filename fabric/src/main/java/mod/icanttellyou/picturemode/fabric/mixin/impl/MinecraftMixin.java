package mod.icanttellyou.picturemode.fabric.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.fabric.client.event.ClientLevelEvents;
import mod.icanttellyou.picturemode.fabric.client.event.OnGameRenderEvents;
import mod.icanttellyou.picturemode.fabric.client.event.RenderTargetBlitEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(
        method = "updateLevelInEngines" /*? >=1.21.11 {*/ + "(Lnet/minecraft/client/multiplayer/ClientLevel;Z)V" /*?}*/,
        at = @At("TAIL")
    )
    private void onLevelUpdate(ClientLevel level, /*? if >=1.21.11 {*/ boolean stopSounds, /*?}*/ CallbackInfo ci) {
        Minecraft client = (Minecraft) (Object) this;
        if (level != null) {
            ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.invoker().afterLevelChange(client, level);
        } else {
            ClientLevelEvents.AFTER_CLIENT_LEVEL_UNLOAD.invoker().afterLevelUnload(client);
        }
    }

    @WrapOperation(
        //? if >=26.1 {
        //method = "renderFrame",
        //? } else {
        method = "runTick",
        //? }
        at = @At(
            value = "INVOKE",
            //? if >=26.2 {
            //target = "Lcom/mojang/blaze3d/systems/GpuSurface;acquireNextTexture()V"
            //? } else {
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
        Operation<Void> original
        *///? } else {
        com.mojang.blaze3d.pipeline.RenderTarget instance, /*? <1.21.5 {*/ /*int width, int height, *//*?}*/ Operation<Void> original
        //? }
    ) {
        if (!RenderTargetBlitEvents.BEFORE.invoker().beforeTargetBlit(instance)) {
            return;
        }

        original.call(instance /*? <1.21.5 {*/ /*, width, height *//*?}*/);
        RenderTargetBlitEvents.AFTER.invoker().afterTargetBlit(instance);
    }

    @WrapOperation(
        //? if >=26.1 {
        //method = "renderFrame",
        //? } else {
        method = "runTick",
        //? }
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;" + /*? >= 26.1 {*/ /*"extract(" *//*? } else {*/ "render(" /*?}*/ +
                //? if >=1.21 {
                "Lnet/minecraft/client/DeltaTracker;"
                //? } else {
                //"FJ"
                //? }
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
