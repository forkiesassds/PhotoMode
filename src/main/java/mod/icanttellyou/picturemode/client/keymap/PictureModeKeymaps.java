package mod.icanttellyou.picturemode.client.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PictureModeKeymaps {
    private static final Set<Pair<KeyMapping, KeyMappingCallback>> KEYMAPS = new HashSet<>();

    public static final KeyMapping OPEN_PICTURE_MODE = register(new KeyMapping(
        "key.picturemode.open",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_M,
        //? if >=1.21.9 {
        KeyMapping.Category.MISC
        //? } else {
        /*KeyMapping.CATEGORY_MISC
        *///? }
    ), (client, mapping) -> {
        while (mapping.consumeClick()) {
            client.setScreen(new PictureModeScreen(null));
        }
    });

    public static KeyMapping register(KeyMapping mapping, KeyMappingCallback callback) {
        KEYMAPS.add(new Pair<>(mapping, callback));
        return mapping;
    }

    public static void initialise(Consumer<KeyMapping> mappingRegistrationCallback) {
        for (Pair<KeyMapping, KeyMappingCallback> keymap : KEYMAPS) {
            KeyMapping mapping = keymap.getFirst();
            mappingRegistrationCallback.accept(mapping);
        }
    }

    public static Consumer<Minecraft> getMappingHandler() {
        return client -> {
            for (Pair<KeyMapping, KeyMappingCallback> keymap : KEYMAPS) {
                KeyMapping mapping = keymap.getFirst();
                KeyMappingCallback callback = keymap.getSecond();

                callback.handleCallback(client, mapping);
            }
        };
    }

    @FunctionalInterface
    public interface KeyMappingCallback {
        void handleCallback(Minecraft client, KeyMapping mapping);
    }
}
