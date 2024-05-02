package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PostEffectProcessor.class)
public interface AccessPostEffectProcessor {
    @Accessor("resourceManager")
    ResourceManager photoMode$getResourceManager();
    @Accessor("passes")
    List<PostEffectPass> photoMode$getPasses();
}
