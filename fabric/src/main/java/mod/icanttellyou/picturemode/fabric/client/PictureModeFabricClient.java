package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.keymap.PictureModeKeymaps;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientLevelEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientRenderEventListeners;
import mod.icanttellyou.picturemode.fabric.client.event.listeners.ClientTickEventListeners;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class PictureModeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLevelEventListeners.initialise();
        ClientRenderEventListeners.initialise();
        ClientTickEventListeners.initialise();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            PictureModeClient.commonInit();

            //? if >=1.21.6
            ResourceLoaderHandlers.initialise();

            //? if <1.20.5 {
            /*//HACK: The Blur mod seems to inject into every screen, including Picture Mode.
            //  We will be injecting our own screen class into the config.
            if (mod.icanttellyou.picturemode.services.PictureModeServices.PLATFORM.isModPresent("blur")) {
                try {
                    Class<?> configClass = Class.forName("com.tterrag.blur.config.BlurConfig");
                    java.lang.reflect.Field exclusions = configClass.getField("blurExclusions");

                    //noinspection unchecked
                    java.util.List<String> blurExclusions = (java.util.List<String>) exclusions.get(null);

                    String pmScreenName = mod.icanttellyou.picturemode.client.gui.PictureModeScreen.class.getName();
                    if (!blurExclusions.contains(pmScreenName))
                        blurExclusions.add(pmScreenName);
                } catch (Exception e) {
                    mod.icanttellyou.picturemode.util.LoggingUtil.log(org.slf4j.event.Level.ERROR,
                        "Failed to add Picture Mode screen to Blur exclusions: ", e);
                }
            }
            *///? }
        });

        PictureModeKeymaps.initialise(KeyBindingHelper::registerKeyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(PictureModeKeymaps.getMappingHandler()::accept);
    }
}
