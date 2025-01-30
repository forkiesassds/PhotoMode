package me.icanttellyou.mods.photomode.fabric.mixin;

import me.icanttellyou.mods.photomode.client.PhotoModeUtils;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = JsonEffectShaderProgram.class, priority = 500)
public class MixinJsonEffectShaderProgram {
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
            )
    )
    Identifier photoMode$correctInitIdentifier(String arg) {
        return PhotoModeUtils.correctIdentifier(arg);
    }

    @Redirect(
            method = "loadEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
            )
    )
    private static Identifier photoMode$correctLoadEffectIdentifier(String arg) {
        return PhotoModeUtils.correctIdentifier(arg);
    }
}
