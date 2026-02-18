package mod.icanttellyou.picturemode.forgelike.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.client.image.screenshot.ScreenshotHandler;
import mod.icanttellyou.picturemode.forgelike.client.event.RenderTargetBlitEvent;
//? if >=1.21.6
import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
//? if >=1.21.6
import net.neoforged.neoforge.client.event.ViewportEvent;
//? } else {
/*import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
*///? }

public final class GameRenderEventListeners {
    @SubscribeEvent
    public void onGameRenderBefore(
            //? if neoforge && >=1.20.5 {
            RenderFrameEvent.Pre
            //? } else {
            /*TickEvent.RenderTickEvent
            *///? }
            event) {
        //? if !neoforge || <1.20.5 {
        /*if (event.phase != TickEvent.Phase.START)
            return;
        *///? }

        Minecraft mc = Minecraft.getInstance();
        ScreenshotHandler screenshotHandler = PictureModeClient.getScreenshotHandler();

        if (screenshotHandler.getStatus() != ScreenshotHandler.Status.WAITING_FOR_FRAME)
            return;

        PictureModeClientConfig config = PictureModeClient.getConfig();
        double multiplier = config.screenshotSettings.resMultiplier;

        int width = (int) (config.screenshotSettings.getWidth() * multiplier);
        int height = (int) (config.screenshotSettings.getHeight() * multiplier);
        screenshotHandler.setWindowResolution(mc.getWindow(), width, height);
        mc.resizeDisplay();

        screenshotHandler.transitionStatus(ScreenshotHandler.Status.WAITING_FOR_RENDER);
    }

    @SubscribeEvent
    public void onGameRenderAfter(
            //? if neoforge && >=1.20.5 {
            RenderFrameEvent.Post
            //? } else {
            /*TickEvent.RenderTickEvent
            *///? }
            event) {
        //? if !neoforge || <1.20.5 {
        /*if (event.phase != TickEvent.Phase.END)
            return;
        *///? }

        Minecraft mc = Minecraft.getInstance();
        ScreenshotHandler screenshotHandler = PictureModeClient.getScreenshotHandler();

        if (screenshotHandler.getStatus() != ScreenshotHandler.Status.WAITING_FOR_RENDER)
            return;

        //? if >=1.21.5 {
        Screenshot.takeScreenshot(mc.getMainRenderTarget(), screenshotHandler.getScreenshotCallback());
        //? } else {
        /*com.mojang.blaze3d.platform.NativeImage screenshot = Screenshot.takeScreenshot(mc.getMainRenderTarget());
        screenshotHandler.getScreenshotCallback().accept(screenshot);
        *///? }
        screenshotHandler.transitionStatus(ScreenshotHandler.Status.CAPTURED);
    }

    @SubscribeEvent
    public void onRenderTargetBlitBefore(RenderTargetBlitEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        ScreenshotHandler screenshotHandler = PictureModeClient.getScreenshotHandler();

        if (screenshotHandler.getStatus() != ScreenshotHandler.Status.CAPTURED)
            return;

        screenshotHandler.restoreCurrentResolution(mc.getWindow());
        mc.resizeDisplay();

        screenshotHandler.transitionStatus(ScreenshotHandler.Status.IDLE);
        event.setCanceled(true);
    }

    //? if neoforge && >=1.21.6 {
    @SubscribeEvent
    public void onFogSetup(ViewportEvent.RenderFog event) {
        if (event.getEnvironment() instanceof PMModifiableFog pmModifiableFog) {
            pmModifiableFog.pictureMode$modifyFog(event.getFogData(), event.getPartialTick());
        }
    }
    //? }
}
