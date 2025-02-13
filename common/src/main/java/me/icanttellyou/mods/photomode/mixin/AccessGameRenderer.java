package me.icanttellyou.mods.photomode.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface AccessGameRenderer {
    @Invoker("method_62904")
    void photoMode$setPostProcessor(Identifier path);
}
