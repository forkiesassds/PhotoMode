package mod.icanttellyou.picturemode.client.image.screenshot;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.client.image.NativeImageWriter;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormat;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.slf4j.event.Level;

import java.io.File;
import java.util.function.Consumer;

public class ScreenshotHandler {
    private static final int FRAME_DELAY = 3;

    private Status status = Status.IDLE;
    private Consumer<NativeImage> screenshotCallback;

    private int curWidth, curHeight;
    private int frame;

    /**
     * Prepares the callbacks for taking a screenshot.
     *
     * @param rootDir         The root directory to save at.
     * @param messageConsumer The message consumer for when the screenshot is saved.
     */
    public void prepareForScreenshot(File rootDir, Consumer<Component> messageConsumer) {
        if (status != Status.IDLE)
            return;

        NativeImageFormat format = PictureModeClient.getConfig().format;
        this.screenshotCallback = image -> {
            File directory = new File(rootDir, Screenshot.SCREENSHOT_DIR);
            directory.mkdir();

            File file = getFile(directory, format);
            Util.ioPool().execute(() -> {
                try (image) {
                    NativeImageWriter.writeToFile(image, format, file);
                    Component component = Component.literal(file.getName())
                            .withStyle(ChatFormatting.UNDERLINE)
                            .withStyle(style -> style.withClickEvent(
                                //? if >=1.21.5 {
                                new ClickEvent.OpenFile(file.getAbsoluteFile())
                                //? } else {
                                /*new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())
                                *///? }
                            ));
                    messageConsumer.accept(Component.translatable("screenshot.success", component));
                } catch (Exception e) {
                    LoggingUtil.log(Level.WARN, "Couldn't save screenshot", e);
                    messageConsumer.accept(Component.translatable("screenshot.failure", e.getMessage()));
                }
            });
        };

        this.transitionStatus(Status.WAITING_FOR_FRAME);
    }

    /**
     * Gets the screenshot callback, for when screenshotting.
     *
     * @return The screenshot callback.
     */
    public Consumer<NativeImage> getScreenshotCallback() {
        return screenshotCallback;
    }

    /**
     * Transitions the current screenshotting status.
     *
     * @param newStatus The new status to transition to.
     */
    public void transitionStatus(Status newStatus) {
        status.validateStatusTransition(newStatus);
        if (newStatus == Status.IDLE)
            frame = 0;

        status = newStatus;
    }

    /**
     * Gets the current screenshotting status.
     *
     * @return The current screenshotting status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Determines if the screenshot should be captured
     *
     * @return Boolean on if the screenshot should be captured
     */
    public boolean shouldCapture() {
        return ++frame >= FRAME_DELAY;
    }

    /**
     * Gets the width, for use of calculating projection matrices
     *
     * @return The width of the viewport.
     */
    public int getWidth() {
        PictureModeClientConfig config = PictureModeClient.getConfig();
        return config.screenshotSettings.fixedWidth != 0
            ? config.screenshotSettings.fixedWidth
            : this.curWidth;
    }

    /**
     * Gets the height, for use of calculating projection matrices
     *
     * @return The height of the viewport.
     */
    public int getHeight() {
        PictureModeClientConfig config = PictureModeClient.getConfig();
        return config.screenshotSettings.fixedHeight != 0
            ? config.screenshotSettings.fixedHeight
            : this.curHeight;
    }

    /**
     * Sets the resolution of the window, and stores it's current resolution.
     *
     * @param window The window to set the resolution of
     * @param width  The width to set
     * @param height The height to set
     */
    public void setWindowResolution(Window window, int width, int height) {
        this.storeCurrentResolution(window);

        window.setWidth(width);
        window.setHeight(height);

        //? if >=26.1 && <26.2
        //((mod.icanttellyou.picturemode.imixin.PMWindowResizeTrick)(Object) window).pictureMode$forciblyResizeWindow();
    }

    private void storeCurrentResolution(Window window) {
        this.curWidth = window.getWidth();
        this.curHeight = window.getHeight();
    }

    /**
     * Restores the current window resolution back to a given window
     *
     * @param window The window to restore its resolution.
     */
    public void restoreCurrentResolution(Window window) {
        window.setWidth(this.curWidth);
        window.setHeight(this.curHeight);

        //? if >=26.1 && <26.2
        //((mod.icanttellyou.picturemode.imixin.PMWindowResizeTrick)(Object) window).pictureMode$forciblyResizeWindow();
    }

    private static File getFile(File rootDir, NativeImageFormat format) {
        String string = Util.getFilenameFormattedDateTime();
        int i = 1;

        while (true) {
            File file = new File(rootDir, string + (i == 1 ? "" : "_" + i) + "." + format.getFormatName());
            if (!file.exists()) {
                return file;
            }

            i++;
        }
    }

    /**
     * The status of the screenshot handler.
     */
    public enum Status {
        IDLE,
        WAITING_FOR_FRAME,
        WAITING_FOR_RENDER,
        CAPTURED;

        /**
         * Validates if the status transition is valid.
         *
         * @param newStatus The new status to transition to.
         */
        public void validateStatusTransition(Status newStatus) {
            Status nextValid = this.getNextValidStatus();

            if (nextValid != newStatus)
                throw new IllegalStateException("Invalid status transition: " +
                        "Tried to transition to " + newStatus + " from " + this + " (expected " + nextValid + ")");
        }

        private Status getNextValidStatus() {
            return switch (this) {
                case IDLE -> WAITING_FOR_FRAME;
                case WAITING_FOR_FRAME -> WAITING_FOR_RENDER;
                case WAITING_FOR_RENDER -> CAPTURED;
                case CAPTURED -> IDLE;
            };
        }
    }
}
