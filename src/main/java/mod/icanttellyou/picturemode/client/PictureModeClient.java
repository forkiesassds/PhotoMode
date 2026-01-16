package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PictureModeClient {
    public static final List<String> PAUSE_SCREEN_CLASSES = List.of(
        "NostalgicPauseScreen"
    );

    private static PictureModeState state;

    /**
     * Gets the current Picture Mode state
     *
     * @return The current Picture Mode state
     */
    public static PictureModeState getState() {
        if (state == null)
            throw new IllegalStateException("Picture Mode state is null!");

        return state;
    }

    public static Button makePictureModeButton(Minecraft client) {
        return Button.builder(Component.translatable("gui.picturemode"), button ->
                client.setScreen(new PictureModeScreen(Component.literal(""))))
            .pos(client.getWindow().getGuiScaledWidth() / 2 - 48, 8)
            .width(98)
            .build();
    }

    public static void onWorldLoad() {
        if (state != null)
            return;

        state = new PictureModeState();
    }

    public static void onWorldExit() {
        state = null;
    }
}
