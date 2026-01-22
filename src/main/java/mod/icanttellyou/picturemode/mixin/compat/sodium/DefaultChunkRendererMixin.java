package mod.icanttellyou.picturemode.mixin.compat.sodium;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = {
    //? if >=1.21
    "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer"
    /*? if >=1.21 && <=1.21.1 {*//*, *//*? }*/
    //? if <=1.21.1
    //"me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer"
}, remap = false)
public abstract class DefaultChunkRendererMixin {
    @Inject(method = "getVisibleFaces", at = @At("HEAD"), remap = false, cancellable = true)
    private static void bypassVisibleFacesCheckInPM(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, CallbackInfoReturnable<Integer> cir) {
        PictureModeState state = PictureModeClient.getState();

        if (state != null && state.isEnabled())
            cir.setReturnValue(-1);
    }
}
