package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {
    @Accessor("postProcessor")
    void photoMode$setPostProcessor(PostEffectProcessor postProcessor);
    @Accessor("postProcessorEnabled")
    void photoMode$setPostProcessorEnabled(boolean enabled);
}
