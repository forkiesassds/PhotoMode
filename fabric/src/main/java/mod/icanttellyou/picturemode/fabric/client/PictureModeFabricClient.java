package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PictureModeClient.config = PictureModeClientConfig.readConfig(FabricLoader.getInstance().getConfigDir());

        ClientPlayConnectionEvents.DISCONNECT.register((listener, mc) ->
                PictureModeClient.onWorldExit());

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.level != null)
                PictureModeClient.getState().tick();
        });
    }
}
