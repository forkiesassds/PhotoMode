package mod.icanttellyou.picturemode.forgelike.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderTarget;
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
    @WrapOperation(
        //? if >=26.1 {
        /*method = "renderFrame",
        *///? } else {
        method = "runTick",
        //? }
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(" +
                //? if <1.21.5
                //"II" +
                ")V"
        )
    )
    private void onScreenBlit(RenderTarget instance, /*? <1.21.5 {*/ /*int width, int height, *//*?}*/ Operation<Void> original) {
        //? if neoforge {
        IEventBus bus = NeoForge.EVENT_BUS;
        //? } else {
        /*IEventBus bus = MinecraftForge.EVENT_BUS;
        *///? }
        RenderTargetBlitEvent.Pre preEvent = new RenderTargetBlitEvent.Pre(instance);
        bus.post(preEvent);
        
        if (preEvent.isCanceled())
            return;

        original.call(instance /*? <1.21.5 {*/ /*, width, height *//*?}*/);
        bus.post(new RenderTargetBlitEvent.Post(instance));
    }
}
