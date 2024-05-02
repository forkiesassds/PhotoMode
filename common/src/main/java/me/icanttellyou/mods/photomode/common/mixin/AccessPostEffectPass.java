package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostEffectPass.class)
public interface AccessPostEffectPass {
    @Accessor("program")
    JsonEffectShaderProgram photoMode$getProgram();
}
