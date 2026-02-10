//? if <1.21.2 {
/*package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.shaders.EffectProgram;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin {
    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;getResourceOrThrow(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/server/packs/resources/Resource;"
        )
    )
    private void useModernShaderLocationBehaviour$jsonRead(
        ResourceProvider resourceProvider,
        String name,
        CallbackInfo ci,
        @Local LocalRef<Identifier> shaderLocation
    ) {
        if (name.indexOf('/') == -1)
            return;

        Identifier parsed = Identifier.tryParse(name);
        if (parsed != null) {
            parsed = parsed.withPrefix("shaders/")
                           .withSuffix(".json");
            shaderLocation.set(parsed);
        }
    }

    @Inject(
        method = "getOrCreate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;getResourceOrThrow(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/server/packs/resources/Resource;"
        )
    )
    private static void useModernShaderLocationBehaviour$shaderRead(
        ResourceProvider resourceProvider,
        Program.Type type,
        String name,
        CallbackInfoReturnable<EffectProgram> cir,
        @Local LocalRef<Identifier> shaderLocation
    ) {
        if (name.indexOf('/') == -1)
            return;

        Identifier parsed = Identifier.tryParse(name);
        if (parsed != null) {
            parsed = parsed.withPrefix("shaders/")
                           .withSuffix(type.getExtension());
            shaderLocation.set(parsed);
        }
    }
}
*///? }