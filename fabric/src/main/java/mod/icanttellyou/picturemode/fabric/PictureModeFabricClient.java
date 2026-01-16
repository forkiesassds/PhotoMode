package mod.icanttellyou.picturemode.fabric;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, server, client) ->
                PictureModeClient.onWorldLoad());
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (client.level == null)
                PictureModeClient.onWorldExit();
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.level != null)
                PictureModeClient.getState().tick();
        });

        ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
    }

    private void afterInitScreen(Minecraft client, Screen screen, int width, int height) {
        if (screen instanceof PauseScreen || PictureModeClient.PAUSE_SCREEN_CLASSES.contains(screen.getClass().getSimpleName())) {
            final List<AbstractWidget> buttons = Screens.getButtons(screen);
            buttons.add(PictureModeClient.makePictureModeButton(client));
        }
    }
}
