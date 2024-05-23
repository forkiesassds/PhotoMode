package me.icanttellyou.mods.photomode.common.client;

import com.google.gson.JsonSyntaxException;
import me.icanttellyou.mods.photomode.common.mixin.AccessGameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Arrays;

import static me.icanttellyou.mods.photomode.common.PhotoModeCommon.LOGGER;

public class PhotoModeUtils {
    private static MinecraftClient client;

    static final Identifier[] SHADER_PROGRAMS = new Identifier[] {
            Identifier.of("photomode", "shaders/post/blur.json"),
            Identifier.of("photomode", "shaders/post/silhouette.json"),
            Identifier.of("photomode", "shaders/post/vignette.json"),
            Identifier.of("photomode", "shaders/post/tiltshift.json"),
            Identifier.of("photomode", "shaders/post/outline.json"),
            Identifier.of("photomode", "shaders/post/outline2.json"),
            Identifier.of("photomode", "shaders/post/eerie.json"),
            Identifier.of("photomode", "shaders/post/sepia.json"),
            Identifier.of("photomode", "shaders/post/inverted.json"),
            Identifier.of("photomode", "shaders/post/distantblur.json")
    };
    static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

    public static void init() {
        LOGGER.info("Photo Mode initialising.");

        client = MinecraftClient.getInstance();
    }

    public static void loadPMPostProcessor(PhotoModeScreen photoModeScreen, GameRenderer gr, Identifier id) {
        if (gr.getPostProcessor() != null) {
            gr.getPostProcessor().close();
        }

        try {
            PhotoModePostEffectProcessor processor = new PhotoModePostEffectProcessor(photoModeScreen, client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id);
            ((AccessGameRenderer) gr).photoMode$setPostProcessor(processor);
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

    /**
     * Corrects wrongly passed identifier path (a/b/namespace:c.json) to (namespace:a/b/c.json).
     * @param path Path to fix
     * @return A {@link Identifier} with correct namespace and path
     */
    public static Identifier correctIdentifier(String path) {
        String[] splitPath = path.split("/"); //a, b, namespace:c.json

        int lastDirColon = splitPath[splitPath.length - 1].indexOf(':');

        if (lastDirColon == -1) {
            return Identifier.ofDefaultNamespace(path);
        } else {
            String[] lastDir = splitPath[splitPath.length - 1].split("\\."); //namespace:c, json
            if (lastDir.length == 1) {
                throw new IllegalArgumentException("Path contains no file extension?");
            }

            Identifier id = Identifier.of(lastDir[0]); //namespace:c, corrects c -> minecraft:c
            return Identifier.of(id.getNamespace(),
                    String.join("/", Arrays.copyOfRange(splitPath, 0, splitPath.length - 1)) + "/" + id.getPath() + "." + lastDir[1]);
        }
    }
}
