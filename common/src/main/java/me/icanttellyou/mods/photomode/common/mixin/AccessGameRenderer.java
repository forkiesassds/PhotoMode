package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {
    @Accessor("postProcessor")
    void photoMode$setPostProcessor(PostEffectProcessor postProcessor);
//    @Accessor("postProcessorEnabled")
//    void photoMode$setPostProcessorEnabled(boolean enabled);
    @Invoker("loadPostProcessor")
    void photoMode$loadPostProcessor(Identifier id);
}
