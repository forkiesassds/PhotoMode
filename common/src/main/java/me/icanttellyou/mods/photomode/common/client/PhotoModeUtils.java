package me.icanttellyou.mods.photomode.common.client;

import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import me.icanttellyou.mods.photomode.common.mixin.AccessGameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.IOException;

public class PhotoModeUtils {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static MinecraftClient client;

    static final Identifier[] SHADER_PROGRAMS = new Identifier[] {
            new Identifier("photomode", "shaders/post/blur.json"),
            new Identifier("photomode", "shaders/post/silhouette.json"),
            new Identifier("photomode", "shaders/post/vignette.json"),
            new Identifier("photomode", "shaders/post/tiltshift.json"),
            new Identifier("photomode", "shaders/post/outline.json"),
            new Identifier("photomode", "shaders/post/outline2.json"),
            new Identifier("photomode", "shaders/post/eerie.json"),
            new Identifier("photomode", "shaders/post/sepia.json"),
            new Identifier("photomode", "shaders/post/inverted.json"),
            new Identifier("photomode", "shaders/post/distantblur.json")
    };
    static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

    public static void init() {
        LOGGER.info("Photo Mode initialising.");

        client = MinecraftClient.getInstance();
    }

    public static void loadPMPostProcessor(GameRenderer gr, Identifier id) {
        if (gr.getPostProcessor() != null) {
            gr.getPostProcessor().close();
        }

        try {
            ((AccessGameRenderer) gr).photoMode$setPostProcessor(
                    new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id));
            gr.getPostProcessor().setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            ((AccessGameRenderer) gr).photoMode$setPostProcessorEnabled(true);
        } catch (IOException ioE) {
            LOGGER.warn("Failed to load shader: {}", id, ioE);
            ((AccessGameRenderer) gr).photoMode$setPostProcessorEnabled(false);
        } catch (JsonSyntaxException jSE) {
            LOGGER.warn("Failed to parse shader: {}", id, jSE);
            ((AccessGameRenderer) gr).photoMode$setPostProcessorEnabled(false);
        }
    }
}
