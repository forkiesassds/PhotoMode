//~ resource_provider
//? if <1.21.2 {
/*package mod.icanttellyou.picturemode.forgelike.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EffectInstance.class, priority = 1001)
public abstract class EffectInstanceMixin {
    @WrapOperation(
        method = "<init>",
        at = @At(
            //? if >=1.21 {
            value = "INVOKE",
            target = "Lnet/minecraft/resources/Identifier;withDefaultNamespace(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;"
            //? } else {
            /^value = "NEW",
            target = "net/minecraft/resources/Identifier"
            ^///? }
        )
    )
    private Identifier useModernShaderLocationBehaviour$jsonRead(
        String s,
        String s2,
        Operation<Identifier> original,
        ResourceProvider resourceProvider,
        String name
    ) {
        if (name.indexOf('/') != -1) {
            Identifier parsed = Identifier.tryParse(name);
            if (parsed != null) {
                parsed = parsed.withPrefix("shaders/")
                        .withSuffix(".json");
                return parsed;
            }
        }

        return original.call(s, s2);
    }

    @WrapOperation(
        method = "getOrCreate",
        at = @At(
            //? if >=1.21 {
            value = "INVOKE",
            target = "Lnet/minecraft/resources/Identifier;withDefaultNamespace(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;"
            //? } else {
            /^value = "NEW",
            target = "net/minecraft/resources/Identifier"
            ^///? }
        )
    )
    private static Identifier useModernShaderLocationBehaviour$shaderRead(
        String s,
        String s2,
        Operation<Identifier> original,
        ResourceProvider resourceProvider,
        Program.Type type,
        String name
    ) {
        if (name.indexOf('/') != -1) {
            Identifier parsed = Identifier.tryParse(name);
            if (parsed != null) {
                parsed = parsed.withPrefix("shaders/")
                        .withSuffix(type.getExtension());
                return parsed;
            }
        }

        return original.call(s, s2);
    }
}
*///? }