package mod.icanttellyou.picturemode.client.image.screenshot;

import com.mojang.blaze3d.platform.NativeImage;

import java.util.ArrayDeque;
import java.util.Queue;

public class ScreenshotHandler {
    private final Queue<NativeImage> imageQueue = new ArrayDeque<>();
    public Status status = Status.IDLE;

    public void captureScreenshot() {
        if (status != Status.IDLE)
            return;
    }

    /**
     * The status of the screenshot handler.
     */
    public enum Status {
        IDLE,
        WAITING_FOR_FRAME,
        CAPTURED;
    }
}
