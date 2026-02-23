package mod.icanttellyou.picturemode.client.render.shader;

//? if >=1.21.6
import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
//? if <1.21.6
//import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;

public class ShaderHandler {
    /**
     * Sets the shader to use.
     *
     * @param shader The identifier for the shader to use.
     */
    public static void setShader(Identifier shader) {
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        //? if <1.21.2 {
        /*if (shader == null) {
            renderer.shutdownEffect();
            return;
        }
        *///? }

        ((mod.icanttellyou.picturemode.mixin.GameRendererAccessor) renderer).invokeSetPostEffect(shader);
        //? if >=1.21.6 {
        PictureModeClient.getShaderPatchHandler().patchShader(shader);
        //? } else {
        /*PostChain shaderEffect = renderer.currentEffect();

        if (shaderEffect != null)
            shaderEffect.setUniform("Intensity", 1.0F);
        *///? }
    }

    /**
     * Sets the intensity of the shader.
     *
     * @param intensity Intensity to set for the shader.
     */
    public static void setIntensity(float intensity) {
        //? if >=1.21.6 {
        PictureModeClient.getShaderPatchHandler().setIntensity(intensity);
        //? } else {
        /*PostChain shader = Minecraft.getInstance().gameRenderer.currentEffect();

        if (shader != null)
            shader.setUniform("Intensity", intensity);
        *///? }
    }
}
