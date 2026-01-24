package mod.icanttellyou.picturemode.fabric.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.client.Minecraft;
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
    private void setupPMState(CallbackInfo ci) {
        PictureModeClient.onWorldLoad();
    }
}
