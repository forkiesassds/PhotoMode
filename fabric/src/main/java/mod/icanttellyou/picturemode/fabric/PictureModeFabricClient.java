package mod.icanttellyou.picturemode.fabric;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> PictureModeClient.onWorldLoad());
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
