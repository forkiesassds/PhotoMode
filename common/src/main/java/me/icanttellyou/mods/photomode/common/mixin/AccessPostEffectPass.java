package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostEffectPass.class)
public interface AccessPostEffectPass {
    @Accessor("field_53927")
    ShaderProgram photoMode$getProgram();
}
