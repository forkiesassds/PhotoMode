package me.icanttellyou.mods.photomode.common.mixin;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public abstract class MixinFrustum {
    @Unique
    MinecraftClient photoMode$client = MinecraftClient.getInstance();

    @Inject(method = "method_38557", at = @At("HEAD"), cancellable = true)
    private void photoMode$coverBoxAroundSetPosition(CallbackInfoReturnable<Frustum> cir) {
        if (photoMode$client.currentScreen instanceof PhotoModeScreen) {
            float div = (float) Math.pow(2.0, ((PhotoModeScreen) this.photoMode$client.currentScreen).getZoom(photoMode$client.getTickDelta()));
            float width = photoMode$client.getWindow().getFramebufferWidth() / div;
            float height = photoMode$client.getWindow().getFramebufferHeight() / div;
            if (width <= 10 || height <= 10) {
                cir.setReturnValue((Frustum) ((Object) this));
            }
        }
    }
}
