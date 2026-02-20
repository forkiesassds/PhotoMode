package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import mod.icanttellyou.picturemode.client.gui.widget.button.AbstractButtonBuilder;
import mod.icanttellyou.picturemode.client.image.screenshot.ScreenshotHandler;
import mod.icanttellyou.picturemode.services.PictureModeServices;
import mod.icanttellyou.picturemode.util.LevelUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PictureModeClient {

    private static PictureModeState state;
    private static final PictureModeClientConfig config =
            PictureModeClientConfig.readConfig(PictureModeServices.PLATFORM.getConfigDir());

    private static final ScreenshotHandler screenshotHandler = new ScreenshotHandler();

    /**
     * Gets the current Picture Mode state
     *
     * @return The current Picture Mode state, or null if not initialised
     */
    public static PictureModeState getState() {
        return state;
    }

    /**
     * Gets the config for Picture Mode.
     *
     * @return The Picture Mode config.
     */
    public static PictureModeClientConfig getConfig() {
        return config;
    }

    /**
     * Gets the screenshot handler for Picture Mode.
     *
     * @return The screenshot handler used by Picture Mode.
     */
    public static ScreenshotHandler getScreenshotHandler() {
        return screenshotHandler;
    }

    /**
     * Gets the width, for use of calculating projection matrices
     *
     * @return The width of the viewport.
     */
    public static int getWidth() {
        if (screenshotHandler.getStatus() == ScreenshotHandler.Status.IDLE)
            return Minecraft.getInstance().getWindow().getWidth();

        return screenshotHandler.getWidth();
    }

    /**
     * Gets the height, for use of calculating projection matrices
     *
     * @return The height of the viewport.
     */
    public static int getHeight() {
        if (screenshotHandler.getStatus() == ScreenshotHandler.Status.IDLE)
            return Minecraft.getInstance().getWindow().getHeight();

        return screenshotHandler.getHeight();
    }

    @SuppressWarnings("unchecked")
    public static <T> T makePMButton(Minecraft minecraft, Screen screen) {
        return (T) AbstractButtonBuilder.getBuilder(screen, Component.translatable("gui.picturemode"),
            button -> minecraft.setScreen(new PictureModeScreen(screen, Component.literal(""))))
            .disableIf(() -> LevelUtils.isPMDisabledForDimension(minecraft.level))
            .pos(screen.width / 2 - 48, 8)
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
