package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.client.keymap.PictureModeKeymaps;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientLevelEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientRenderEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientTickEventListeners;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLevelEventListeners.initialise();
        ClientRenderEventListeners.initialise();
        ClientTickEventListeners.initialise();

        PictureModeKeymaps.initialise(KeyBindingHelper::registerKeyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(PictureModeKeymaps.getMappingHandler()::accept);
    }
}
