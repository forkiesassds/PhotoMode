//? if >=1.21.6 {
package mod.icanttellyou.picturemode.client.render.shader;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.icanttellyou.picturemode.PictureMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class ShaderPatchHandler implements ResourceManagerReloadListener {
    public static final Identifier RELOAD_LISTENER_ID = PictureMode.createId("shader_patch_handler");
    private static final String UBO_NAME = "PMConfig";

    private GpuBuffer intensityUniform = this.makeIntensityUniform();

    private final Set<Identifier> patchedShaders = new HashSet<>();
    private float intensity = 1.0F;

    private GpuBuffer makeIntensityUniform() {
        return RenderSystem.getDevice()
            .createBuffer(() -> "PM Intensity Uniform", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, 4);
    }

    /**
     * Patch a shader to use the custom Intensity uniform.
     *
     * @param shader The shader to patch.
     */
    public void patchShader(Identifier shader) {
        if (shader == null || this.patchedShaders.contains(shader))
            return;

        PostChain chain = Minecraft.getInstance().getShaderManager()
                .getPostChain(shader, LevelTargetBundle.MAIN_TARGETS);
        ShaderUtil.patchUniforms(chain, UBO_NAME, this.intensityUniform);
        this.patchedShaders.add(shader);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
     * Writes the UBO into the intensity uniform buffer.
     */
    public void writeUBO() {
        if (this.intensityUniform.isClosed())
            return;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer byteBuffer = Std140Builder.onStack(memoryStack, 4)
                    .putFloat(this.intensity)
                    .get();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.intensityUniform.slice(), byteBuffer);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        if (!this.intensityUniform.isClosed())
            this.intensityUniform.close();

        this.intensityUniform = this.makeIntensityUniform();
        this.patchedShaders.clear();
    }
}
//? }