package mod.icanttellyou.picturemode.fabric;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(
            (listener, sender, mc) -> PictureModeClient.onWorldLoad());
        ClientPlayConnectionEvents.DISCONNECT.register(
            (listener, mc) -> PictureModeClient.onWorldExit());
    }
}
