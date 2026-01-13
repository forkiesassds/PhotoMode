package mod.icanttellyou.picturemode.fabric;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

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
    }
}
