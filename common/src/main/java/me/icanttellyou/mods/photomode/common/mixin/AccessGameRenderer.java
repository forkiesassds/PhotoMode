package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {
    @Accessor("shader")
    void photoMode$setPostProcessor(ShaderEffect postProcessor);
    @Accessor("shadersEnabled")
    void photoMode$setPostProcessorEnabled(boolean enabled);
}
