package mod.icanttellyou.picturemode.client.render.shader;

//? if >=1.21.6
import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;

public class ShaderHandler {
    /**
     * Sets the shader to use.
     *
     * @param shader The identifier for the shader to use.
     */
    public static void setShader(Identifier shader) {
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        if (shader == null) {
            //? if >=26.3 {
            //((mod.icanttellyou.picturemode.imixin.PMPostEffectApplier) renderer).pm$clearPostEffect();
            //? } else if >=1.21.2 {
            renderer.clearPostEffect();
            //? } else {
            //renderer.shutdownEffect();
            //? }
            return;
        }

        //? if >=26.3 {
        //((mod.icanttellyou.picturemode.imixin.PMPostEffectApplier) renderer).pm$setPostEffect(shader);
        //? } else {
        ((mod.icanttellyou.picturemode.mixin.GameRendererAccessor) renderer).invokeSetPostEffect(shader);
        //? }
        //? if >=1.21.6 {
        PictureModeClient.getShaderPatchHandler().patchShader(shader);
        //? } else {
        //setIntensity(1.0F);
        //? }
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
        //setUniform("Intensity", intensity);
        //? }
    }

    //? if >=1.21.6
    @SuppressWarnings("unused")
    private static void setUniform(String uniform, float value) {
        //? if >=1.21.6 {
        throw new RuntimeException("Direct uniform setting is unsupported in 1.21.6+");
        //? } else {
        /*net.minecraft.client.renderer.PostChain shader = Minecraft.getInstance().gameRenderer.currentEffect();

        if (shader != null) {
            //? if >=1.20.5 {
            shader.setUniform(uniform, value);
            //? } else {
            /^for (net.minecraft.client.renderer.PostPass postPass : ((mod.icanttellyou.picturemode.mixin.PostChainAccessor) shader).getPasses()) {
                postPass.getEffect().safeGetUniform(uniform).set(value);
            }
            ^///? }
        }
        *///? }
    }
}
