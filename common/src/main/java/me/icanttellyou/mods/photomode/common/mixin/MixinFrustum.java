package me.icanttellyou.mods.photomode.common.mixin;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public class MixinFrustum {
    //this kills frustum culling to fix an issue where the game hangs due to it trying to calculate the frustum intersections
    //bad hack for a bad problem, PLEASE REPLACE THIS WITH A BETTER SOLUTION!!!!!!!!!!!
    @Inject(method = "isVisible(DDDDDD)Z", at = @At("HEAD"), cancellable = true)
    private void injectIsVisible(CallbackInfoReturnable<Boolean> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "coverBoxAroundSetPosition", at = @At("HEAD"), cancellable = true)
    private void photoMode$coverBoxAroundSetPosition(CallbackInfoReturnable<Frustum> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            cir.setReturnValue((Frustum)((Object)this));
            cir.cancel();
        }
    }
}
