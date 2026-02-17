package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientLevelEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientTickEventListeners;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PictureModeClient.config = PictureModeClientConfig.readConfig(FabricLoader.getInstance().getConfigDir());

        ClientLevelEventListeners.initialise();
        ClientTickEventListeners.initialise();
    }
}
