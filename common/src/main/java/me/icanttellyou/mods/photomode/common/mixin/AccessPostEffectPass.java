package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.client.gl.PostProcessShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostProcessShader.class)
public interface AccessPostEffectPass {
    @Accessor("program")
    JsonEffectGlShader photoMode$getProgram();
}
