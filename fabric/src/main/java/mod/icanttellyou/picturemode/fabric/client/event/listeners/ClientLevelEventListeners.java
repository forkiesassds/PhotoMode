package mod.icanttellyou.picturemode.fabric.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.fabric.client.event.ClientLevelEvents;

public final class ClientLevelEventListeners {
    public static void initialise() {
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((mc, level) ->
                PictureModeClient.onWorldLoad());
        ClientLevelEvents.AFTER_CLIENT_LEVEL_UNLOAD.register(mc ->
                PictureModeClient.onWorldExit());
    }
}
