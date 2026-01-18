package mod.icanttellyou.picturemode.mixin.compat.sodium;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = {
    //? if >=1.21
    "net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager"
    /*? if >=1.21 && <=1.21.1 {*//*, *//*? }*/
    //? if <=1.21.1
    //"me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager"
}, remap = false)
public abstract class RenderSectionManagerMixin {
    @Definition(id = "smartCull", field = "Lnet/minecraft/client/Minecraft;smartCull:Z")
    @Definition(id = "getInstance", method = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;")
    @Expression("getInstance().smartCull")
    @ModifyExpressionValue(method = "shouldUseOcclusionCulling", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean disableSmartCullInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();
        return !state.isEnabled() && original;
    }
}