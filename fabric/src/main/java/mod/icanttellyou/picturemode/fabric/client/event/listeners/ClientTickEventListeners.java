package mod.icanttellyou.picturemode.fabric.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class ClientTickEventListeners {
    public static void initialise() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PictureModeState state = PictureModeClient.getState();

            if (client.level != null && state != null)
                state.tick();
        });
    }
}
