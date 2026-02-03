package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import mod.icanttellyou.picturemode.services.PictureModeServices;
import mod.icanttellyou.picturemode.util.LevelUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PictureModeClient {
    public static final boolean HAS_SODIUM = PictureModeServices.PLATFORM.isModPresent("sodium");
    public static final boolean HAS_VULKANMOD = PictureModeServices.PLATFORM.isModPresent("vulkanmod");

    private static PictureModeState state;

    /**
     * Gets the current Picture Mode state
     *
     * @return The current Picture Mode state, or null if not initialised
     */
    public static PictureModeState getState() {
        return state;
    }

    public static Button makePMButton(Minecraft minecraft, Screen screen) {
        Button pmButton = Button.builder(Component.translatable("gui.picturemode"),
            button -> {
                if (LevelUtils.isPMDisabledForDimension(minecraft.level)) {
                    button.active = false;
                    return;
                }

                minecraft.setScreen(new PictureModeScreen(screen, Component.literal("")));
            })
            .pos(screen.width / 2 - 48, 8)
            .width(98)
            .build();

        boolean disabled = LevelUtils.isPMDisabledForDimension(minecraft.level);

        pmButton.active = !disabled;
        return pmButton;
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
