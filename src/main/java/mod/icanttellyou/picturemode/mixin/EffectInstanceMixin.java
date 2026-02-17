//~ resource_provider
//? if <1.21.2 {
/*package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin {
    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;getResourceOrThrow(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/server/packs/resources/Resource;"
        )
    )
    private Resource useModernShaderLocationBehaviour$jsonRead(
        ResourceProvider instance,
        Identifier resourceLocation,
        Operation<Resource> original,
        ResourceProvider resourceProvider,
        String name
    ) {
        if (name.indexOf('/') != -1) {
            Identifier parsed = Identifier.tryParse(name);
            if (parsed != null) {
                parsed = parsed.withPrefix("shaders/")
                        .withSuffix(".json");
                resourceLocation = parsed;
            }
        }

        return original.call(instance, resourceLocation);
    }

    @WrapOperation(
        method = "getOrCreate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;getResourceOrThrow(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/server/packs/resources/Resource;"
        )
    )
    private static Resource useModernShaderLocationBehaviour$shaderRead(
        ResourceProvider instance,
        Identifier resourceLocation,
        Operation<Resource> original,
        ResourceProvider resourceProvider,
        Program.Type type,
        String name
    ) {
        if (name.indexOf('/') != -1) {
            Identifier parsed = Identifier.tryParse(name);
            if (parsed != null) {
                parsed = parsed.withPrefix("shaders/")
                        .withSuffix(type.getExtension());
                resourceLocation = parsed;
            }
        }

        return original.call(instance, resourceLocation);
    }
}
*///? }