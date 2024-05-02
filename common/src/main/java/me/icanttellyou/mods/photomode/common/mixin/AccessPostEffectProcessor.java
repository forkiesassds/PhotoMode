package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShaderEffect.class)
public interface AccessPostEffectProcessor {
    @Accessor("resourceManager")
    ResourceManager photoMode$getResourceManager();
    @Accessor("passes")
    List<PostProcessShader> photoMode$getPasses();
}
