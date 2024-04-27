package me.icanttellyou.mods.photomode.common.client;

import net.minecraft.util.Identifier;

public class PhotoModeUtils {
//    public static final Logger LOGGER = LogUtils.getLogger();
//    private static final MinecraftClient client = MinecraftClient.getInstance();

    static final Identifier[] SHADER_PROGRAMS = new Identifier[] {
            new Identifier("photomode", "shaders/post/blur.json"),
            new Identifier("photomode", "shaders/post/distantblur.json"),
            new Identifier("photomode", "shaders/post/eerie.json"),
            new Identifier("photomode", "shaders/post/inverted.json"),
            new Identifier("photomode", "shaders/post/outline.json"),
            new Identifier("photomode", "shaders/post/outline2.json"),
            new Identifier("photomode", "shaders/post/sepia.json"),
            new Identifier("photomode", "shaders/post/silhouette.json"),
            new Identifier("photomode", "shaders/post/tiltshift.json"),
            new Identifier("photomode", "shaders/post/vignette.json")
    };

    static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

    /*private void loadPMPostProcessor(Identifier id) {
        if (client.gameRenderer.getPostProcessor() != null) {
            client.gameRenderer.getPostProcessor().close();
        }

        try {
            ((AccessGameRenderer) client.gameRenderer).photoMode$setPostProcessor(new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id));
            client.gameRenderer.getPostProcessor().setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            ((AccessGameRenderer) client.gameRenderer).photoMode$setPostProcessorEnabled(true);
        } catch (IOException var3) {
            LOGGER.warn("Failed to load shader: {}", id, var3);
            ((AccessGameRenderer) client.gameRenderer).photoMode$setPostProcessorEnabled(false);
        } catch (JsonSyntaxException var4) {
            LOGGER.warn("Failed to parse shader: {}", id, var4);
            ((AccessGameRenderer) client.gameRenderer).photoMode$setPostProcessorEnabled(false);
        }
    }*/
}
