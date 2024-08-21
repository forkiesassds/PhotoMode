package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {
    @Accessor("field_53898")
    void photoMode$setPostProcessor(Identifier postProcessor);
    @Accessor("postProcessorEnabled")
    void photoMode$setPostProcessorEnabled(boolean enabled);
}
