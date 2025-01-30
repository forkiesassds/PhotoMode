package me.icanttellyou.mods.photomode.mixin.compat;

import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
    "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer",
    "me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer"
}, remap = false)
public abstract class MixinDefaultChunkRenderer {
    @Inject(method = "getVisibleFaces", at = @At("HEAD"), remap = false, cancellable = true)
    private static void photoMode$sodium05x$getVisibleFaces(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, CallbackInfoReturnable<Integer> cir) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen)
            cir.setReturnValue(-1);
    }
}
