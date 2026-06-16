package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import mod.icanttellyou.picturemode.client.gui.widget.button.AbstractButtonBuilder;
import mod.icanttellyou.picturemode.client.image.screenshot.ScreenshotHandler;
//? if >=1.21.6
import mod.icanttellyou.picturemode.client.render.shader.ShaderPatchHandler;
import mod.icanttellyou.picturemode.services.PictureModeServices;
import mod.icanttellyou.picturemode.util.LevelUtils;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PictureModeClient {

    private static PictureModeState state;
    private static final PictureModeClientConfig config =
            PictureModeClientConfig.readConfig(PictureModeServices.PLATFORM.getConfigDir());

    private static final ScreenshotHandler screenshotHandler = new ScreenshotHandler();
    //? if >=1.21.6
    private static ShaderPatchHandler shaderPatchHandler;

    public static void commonInit() {
        if (PictureModeServices.PLATFORM.isModPresent("firstperson")) {
            /*
             * What good is an API that is proprietary? What good does it serve for the developer?
             * Making a proprietary API is a huge loss towards Open Source developers and the community.
             * This horrid mess is a result of all of this.
             */
            try {
                Class<?> api = Class.forName("dev.tr7zw.firstperson.api.FirstPersonAPI");
                Class<?> activationHandler = Class.forName("dev.tr7zw.firstperson.api.ActivationHandler");
                Method register = api.getMethod("registerPlayerHandler", Object.class);
                Object handler = Proxy.newProxyInstance(activationHandler.getClassLoader(), new Class[] {activationHandler},
                    (o, m, args) -> {
                        if (m.getName().equals("preventFirstperson"))
                            return state != null && state.isEnabled();

                        throw new RuntimeException("Proxy invoked for unhandled method " + m.getName() + ". This should NOT happen!");
                    });

                register.invoke(null, handler);
            } catch (Exception e) {
                LoggingUtil.log(Level.ERROR, "Failed to initialise FirstPerson compatibility: ", e);
            }
        }

        //? if <1.20.5 {
        /*//HACK: The Blur mod seems to inject into every screen, including Picture Mode.
        //  We will be injecting our own screen class into the config.
        if (PictureModeServices.PLATFORM.isModPresent("blur")) {
            try {
                Class<?> configClass = Class.forName("com.tterrag.blur.config.BlurConfig");
                java.lang.reflect.Field exclusions = configClass.getField("blurExclusions");

                //noinspection unchecked
                java.util.List<String> blurExclusions = (java.util.List<String>) exclusions.get(null);

                String pmScreenName = PictureModeScreen.class.getName();
                if (!blurExclusions.contains(pmScreenName))
                    blurExclusions.add(pmScreenName);
            } catch (Exception e) {
                LoggingUtil.log(Level.ERROR, "Failed to add Picture Mode screen to Blur exclusions: ", e);
            }
        }
        *///? }
    }

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

    //? if >=1.21.6 {

    /**
     * Initialises the shader patch handler.
     */
    public static void initialiseShaderPatchHandler() {
        if (shaderPatchHandler == null)
            shaderPatchHandler = new ShaderPatchHandler();
    }

    /**
     * Gets the shader patch handler for Picture Mode.
     *
     * @return The shader patch handler used by Picture Mode.
     */
    public static ShaderPatchHandler getShaderPatchHandler() {
        return shaderPatchHandler;
    }
    //? }

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
            button -> minecraft.setScreen(new PictureModeScreen(screen)))
            .disableIf(() -> LevelUtils.isPMDisabledForDimension(minecraft.level))
            .pos(screen.width / 2 - 48, 8)
            .width(98)
            .build();
    }

    public static boolean isSingleplayer(Minecraft minecraft) {
        //? if >=26.2 {
        /*return !minecraft.isMultiplayerServer();
        *///? } else {
        return minecraft.isSingleplayer();
        //? }
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
