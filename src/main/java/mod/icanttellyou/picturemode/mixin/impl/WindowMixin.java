//? if >=26.1 && <26.2 {
/*package mod.icanttellyou.picturemode.mixin.impl;

import com.mojang.blaze3d.platform.Window;
import mod.icanttellyou.picturemode.imixin.PMWindowResizeTrick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public abstract class WindowMixin implements PMWindowResizeTrick {
    @Unique private boolean pictureMode$forceResize;

    @Inject(method = "isResized", at = @At("HEAD"), cancellable = true)
    private void forceResizeWindow(CallbackInfoReturnable<Boolean> cir) {
        if (this.pictureMode$forceResize) {
            this.pictureMode$forceResize = false;
            cir.setReturnValue(true);
        }
    }

    @Override
    public void pictureMode$forciblyResizeWindow() {
        this.pictureMode$forceResize = true;
    }
}
*///? }