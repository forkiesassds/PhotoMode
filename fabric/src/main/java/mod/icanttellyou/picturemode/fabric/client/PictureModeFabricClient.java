package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientLevelEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientRenderEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientTickEventListeners;
import net.fabricmc.api.ClientModInitializer;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLevelEventListeners.initialise();
        ClientRenderEventListeners.initialise();
        ClientTickEventListeners.initialise();
    }
}
