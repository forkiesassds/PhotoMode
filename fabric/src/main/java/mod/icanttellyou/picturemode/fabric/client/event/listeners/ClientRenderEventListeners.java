package mod.icanttellyou.picturemode.fabric.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.client.image.screenshot.ScreenshotHandler;
import mod.icanttellyou.picturemode.fabric.client.event.OnGameRenderEvents;
import mod.icanttellyou.picturemode.fabric.client.event.RenderTargetBlitEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

public final class ClientRenderEventListeners {
    public static void initialise() {
        Minecraft mc = Minecraft.getInstance();
        ScreenshotHandler screenshotHandler = PictureModeClient.getScreenshotHandler();

        OnGameRenderEvents.BEFORE.register((renderer, renderLevel) -> {
            if (screenshotHandler.getStatus() != ScreenshotHandler.Status.WAITING_FOR_FRAME)
                return;

            PictureModeClientConfig config = PictureModeClient.getConfig();
            double multiplier = config.screenshotSettings.resMultiplier;

            int width = (int) (config.screenshotSettings.getWidth() * multiplier);
            int height = (int) (config.screenshotSettings.getHeight() * multiplier);
            screenshotHandler.setWindowResolution(mc.getWindow(), width, height);
            mc.resizeDisplay();

            screenshotHandler.transitionStatus(ScreenshotHandler.Status.WAITING_FOR_RENDER);
        });
        OnGameRenderEvents.AFTER.register((renderer, renderLevel) -> {
            if (screenshotHandler.getStatus() != ScreenshotHandler.Status.WAITING_FOR_RENDER)
                return;

            Screenshot.takeScreenshot(mc.getMainRenderTarget(), screenshotHandler.getScreenshotCallback());
            screenshotHandler.transitionStatus(ScreenshotHandler.Status.CAPTURED);
        });

        RenderTargetBlitEvents.BEFORE.register(target -> {
            if (screenshotHandler.getStatus() != ScreenshotHandler.Status.CAPTURED)
                return true;

            screenshotHandler.restoreCurrentResolution(mc.getWindow());
            mc.resizeDisplay();

            screenshotHandler.transitionStatus(ScreenshotHandler.Status.IDLE);
            return false;
        });
    }
}
