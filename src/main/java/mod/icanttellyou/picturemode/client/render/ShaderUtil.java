package mod.icanttellyou.picturemode.client.render;

import mod.icanttellyou.picturemode.PictureMode;
import net.minecraft.resources.Identifier;
//? if >=1.21.6 {
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.icanttellyou.picturemode.mixin.PostChainAccessor;
import mod.icanttellyou.picturemode.mixin.PostPassAccessor;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;

import java.util.List;
import java.util.Map;
//? }

public class ShaderUtil {
    static final Identifier[] SHADER_PROGRAMS = new Identifier[] {
        getShaderId("blur"),
        getShaderId("silhouette"),
        getShaderId("vignette"),
        getShaderId("tiltshift"),
        getShaderId("outline"),
        getShaderId("outline2"),
        getShaderId("eerie"),
        getShaderId("sepia"),
        getShaderId("inverted"),
        getShaderId("distantblur")
    };
    static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

    //? if >=1.21.6 {
    private static final GpuBuffer intensityUniform = RenderSystem.getDevice()
            .createBuffer(() -> "PM Intensity Uniform", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE, 4);

    private static PostChain patchUniforms(PostChain p, String name, GpuBuffer uniformsBuffer) {
        List<PostPass> passes = ((PostChainAccessor) p).getPasses();
        for (PostPass pass : passes) {
            Map<String, GpuBuffer> uniforms = ((PostPassAccessor) pass).getCustomUniforms();
            GpuBuffer bc = uniforms.get(name);
            bc.close();
            uniforms.put(name, uniformsBuffer);
        }
        return p;
    }
    //? }

    private static Identifier getShaderId(String name) {
        //? if >=1.21.2 {
        return PictureMode.createId(name);
        //? } else {
        /*return PictureMode.createId("post_effect/" + name + ".json");
        *///? }
    }
}
