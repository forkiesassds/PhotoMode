package me.icanttellyou.mods.photomode.client;

import me.icanttellyou.mods.photomode.mixin.AccessGameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Arrays;

import static me.icanttellyou.mods.photomode.PhotoModeCommon.LOGGER;

public class PhotoModeUtils {
    private static MinecraftClient client;

    static final Identifier[] SHADER_PROGRAMS = new Identifier[] {
            Identifier.of("photomode", "blur"),
            Identifier.of("photomode", "silhouette"),
            Identifier.of("photomode", "vignette"),
            Identifier.of("photomode", "tiltshift"),
            Identifier.of("photomode", "outline"),
            Identifier.of("photomode", "outline2"),
            Identifier.of("photomode", "eerie"),
            Identifier.of("photomode", "sepia"),
            Identifier.of("photomode", "inverted"),
            Identifier.of("photomode", "distantblur")
    };
    static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

    public static void init() {
        LOGGER.info("Photo Mode initialising.");

        client = MinecraftClient.getInstance();
    }

    public static void loadPMPostProcessor(PhotoModeScreen photoModeScreen, GameRenderer gr, Identifier id) {
        gr.clearPostProcessor();
        ((AccessGameRenderer) gr).photoMode$setPostProcessor(id);
    }
}
