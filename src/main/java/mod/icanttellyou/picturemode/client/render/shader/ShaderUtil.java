package mod.icanttellyou.picturemode.client.render.shader;

import mod.icanttellyou.picturemode.PictureMode;
import net.minecraft.client.Minecraft;
//? if >=1.21.6 {
import com.mojang.blaze3d.buffers.GpuBuffer;
import mod.icanttellyou.picturemode.mixin.PostChainAccessor;
import mod.icanttellyou.picturemode.mixin.PostPassAccessor;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;

import java.util.List;
import java.util.Map;
//? }

public class ShaderUtil {
    public static final ShaderHolder[] SHADER_PROGRAMS = new ShaderHolder[] {
        ShaderHolder.EMPTY,
        create("blur"),
        create("silhouette"),
        create("vignette"),
        create("tiltshift"),
        create("outline"),
        create("outline2"),
        create("eerie"),
        create("sepia"),
        create("inverted"),
        create("distantblur")
    };

    //? if >=1.21.6 {
    public static PostChain patchUniforms(PostChain p, String name, GpuBuffer uniformsBuffer) {
        List<PostPass> passes = ((PostChainAccessor) p).getPasses();
        for (PostPass pass : passes) {
            Map<String, GpuBuffer> uniforms = ((PostPassAccessor) pass).getCustomUniforms();
            if (!uniforms.containsKey(name))
                continue;

            GpuBuffer bc = uniforms.get(name);
            bc.close();
            uniforms.put(name, uniformsBuffer);
        }
        return p;
    }
    //? }

    private static ShaderHolder create(String id) {
        return new ShaderHolder(PictureMode.createId(id));
    }

    public static boolean useShaderTransparency() {
        //? if >=26.2 {
        /*Minecraft minecraft = Minecraft.getInstance();
        //? if >=26.3 {
        /^return minecraft.gameRenderer.useImprovedTransparency();
        ^///? } else {
        return minecraft.gameRenderer.gameRenderState().useShaderTransparency();
        //? }
        *///? } else {
        return Minecraft.useShaderTransparency();
        //? }
    }
}
