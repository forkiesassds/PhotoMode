package mod.icanttellyou.picturemode.client.gui;

import com.mojang.blaze3d.platform.Window;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.gui.layout.AnchorLayout;
import mod.icanttellyou.picturemode.client.gui.widget.FadingStringWidget;
import mod.icanttellyou.picturemode.client.gui.widget.Slider;
import mod.icanttellyou.picturemode.client.image.screenshot.ScreenshotHandler;
import mod.icanttellyou.picturemode.client.render.shader.ShaderHolder;
import mod.icanttellyou.picturemode.client.render.shader.ShaderUtil;
import mod.icanttellyou.picturemode.util.LevelUtils;
import mod.icanttellyou.picturemode.util.Tickable;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import static mod.icanttellyou.picturemode.PictureModeConstants.*;

public class PictureModeScreen extends Screen {
    private static final String DEFAULT_KEY = "gui.picturemode.default";
    private static final String DEGREES_KEY = "gui.picturemode.degrees";

    private static final String CENTER_CAMERA_KEY = "gui.picturemode.centerCamera";
    private static final String SHOW_PLAYER_KEY = "gui.picturemode.showPlayer";
    private static final String TIME_KEY = "gui.picturemode.time";
    private static final String FOG_KEY = "gui.picturemode.fog";
    private static final String TILT_KEY = "gui.picturemode.tilt";
    private static final String SHADER_KEY = "gui.picturemode.shader";
    private static final String INTENSITY_KEY = "gui.picturemode.intensity";

    private static final String TAKE_SCREENSHOT_KEY = "gui.picturemode.takeScreenshot";
    private static final String HELP_TEXT_KEY = "gui.picturemode.helpText";

    private final PictureModeState pmState;

    private final Screen parent;
    private final AnchorLayout layout;
    private FadingStringWidget helpText;
    private Button centerCameraButton;

    private final long openedAtMillis;

    private double cameraPanXStart;
    private double cameraPanYStart;
    private double cameraRotationStart;

    private double mouseXStart;
    private double mouseYStart;

    private @Nullable Runnable textDelegate;

    public PictureModeScreen(Screen parent) {
        super(Component.empty());
        this.parent = parent;
        this.pmState = PictureModeClient.getState();
        this.layout = new AnchorLayout(0, 0);

        this.openedAtMillis = Util.getMillis();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (PictureModeClient.getScreenshotHandler().getStatus() != ScreenshotHandler.Status.IDLE)
            return;

        if (textDelegate != null) {
            textDelegate.run();
            textDelegate = null;
        }

        centerCameraButton.active =
            (pmState.cameraPanX.getValue(partialTick) != 0.0D || pmState.cameraPanY.getValue(partialTick) != 0.0D) &&
                    (pmState.cameraPanX.getGoal() != 0.0D || pmState.cameraPanY.getGoal() != 0.0D);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics /*? >=1.20.2 {*/, int mouseX, int mouseY, float partialTick/*?}*/) {}

    @Override
    protected void init() {
        super.init();

        if (!PictureModeClient.getConfig().preserveSettings) {
            this.pmState.resetState();
        }
        this.pmState.setEnabled(true);

        Button closeButton = Button.builder(Component.literal("X"), button ->
                minecraft.setScreen(this.parent))
            .width(20)
            .build();
        this.layout.addChild(AnchorLayout.Position.TOP_LEFT, closeButton);

        GridLayout options = makeOptions();
        this.layout.addChild(AnchorLayout.Position.TOP_RIGHT, options);

        GridLayout actions = makeActions();
        this.layout.addChild(AnchorLayout.Position.BOTTOM_CENTER, actions);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    private GridLayout makeOptions() {
        GridLayout layout = new GridLayout();
        layout.defaultCellSetting().paddingBottom(1);

        GridLayout.RowHelper rows = layout.createRowHelper(1);

        rows.addChild(centerCameraButton = Button.builder(Component.translatable(CENTER_CAMERA_KEY),
            button -> {
                pmState.cameraPanX.setGoal(0.0D, this.getDeltaTicks());
                pmState.cameraPanY.setGoal(0.0D, this.getDeltaTicks());
            })
            .build());
        rows.addChild(Button.builder(Component.translatable(SHOW_PLAYER_KEY,
                                CommonComponents.optionStatus(pmState.isPlayerShown())),
            button -> {
                pmState.togglePlayerShown();
                button.setMessage(Component.translatable(SHOW_PLAYER_KEY,
                        CommonComponents.optionStatus(pmState.isPlayerShown())));
            })
            .build());
        rows.addChild(new Slider(0, 0, DEFAULT_TILT / TILT_ANGLES,
            (slider, value, messageUpdate) -> {
                if (!messageUpdate) {
                    pmState.cameraTilt.setGoal(value * TILT_ANGLES, this.getDeltaTicks());
                } else {
                    int degrees = (int) (value * TILT_ANGLES);
                    slider.setMessage(Component.translatable(TILT_KEY, degrees == DEFAULT_TILT
                        ? Component.translatable(DEFAULT_KEY)
                        : Component.translatable(DEGREES_KEY, degrees)));
                }
            }));
        if (minecraft.isSingleplayer()) {
            rows.addChild(new Slider(0, 0, 0.0D,
                (slider, value, messageUpdate) -> {
                    assert minecraft.level != null;
                    int dayLength = LevelUtils.getDayLength(minecraft.level);

                    if (!messageUpdate) {
                        pmState.timeOverride.setGoal(value * dayLength);
                    } else {
                        int ticks = (int) (value * dayLength);
                        slider.setMessage(Component.translatable(TIME_KEY, ticks == 0
                            ? Component.translatable(DEFAULT_KEY)
                            : Component.literal(String.valueOf(ticks))));
                    }
                }));
            rows.addChild(new Slider(0, 0, 1.0D,
                (slider, value, messageUpdate) -> {
                    if (!messageUpdate) {
                        pmState.fog.setGoal(Math.pow(2.0D, 8.0D * value - 8.0D), this.getDeltaTicks());
                    } else {
                        int percent = (int) (value * 100.0D);
                        slider.setMessage(Component.translatable(FOG_KEY, percent));
                    }
                }));
        }

        Slider intensitySlider = new Slider(0, 0, 1.0D,
            (slider, value, messageUpdate) -> {
                if (!messageUpdate) {
                    pmState.setShaderIntensity(value);
                } else {
                    int percent = (int) (value * 100.0D);
                    slider.setMessage(Component.translatable(INTENSITY_KEY, percent));
                }
            });
        CycleButton<ShaderHolder> shaderButton;

        ShaderHolder shaderHolder = pmState.getShader();
        rows.addChild(shaderButton = CycleButton.builder(ShaderHolder::getTranslatedName /*? >=1.21.11 {*/, shaderHolder /*?}*/)
            .withValues(ShaderUtil.SHADER_PROGRAMS)
            //? if <1.21.11
            //.withInitialValue(shaderHolder)
            .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable(SHADER_KEY),
                (button, holder) -> {
                    intensitySlider.active = holder.id() != null;

                    //? if <1.21.2 {
                    /*net.minecraft.client.renderer.GameRenderer renderer = this.minecraft.gameRenderer;
                    if (renderer.currentEffect() != null)
                        renderer.shutdownEffect();
                    *///? }

                    pmState.setShader(holder);
                }));
        rows.addChild(intensitySlider);

        if (Minecraft.useShaderTransparency()) {
            shaderButton.active = false;
            shaderButton.setTooltip(Tooltip.create(Component.translatable(SHADER_KEY + ".incompatible")));
        }

        if (shaderHolder.id() == null) {
            intensitySlider.active = false;
        }

        return layout;
    }

    private GridLayout makeActions() {
        GridLayout layout = new GridLayout();
        layout.defaultCellSetting().alignHorizontallyCenter();

        GridLayout actions = new GridLayout();
        actions.defaultCellSetting()
            .alignHorizontallyCenter()
            .paddingHorizontal(1);

        GridLayout.RowHelper columns = layout.createRowHelper(1);
        GridLayout.RowHelper rows = actions.createRowHelper(3);

        this.helpText = new FadingStringWidget(Component.translatable(HELP_TEXT_KEY),
            this.font, SharedConstants.TICKS_PER_SECOND * 5, true);
        //? if >=1.21.11 {
        this.helpText.setComponentClickHandler(style ->
            defaultHandleGameClickEvent(style.getClickEvent(), minecraft, this));
        //? } else {
        /*this.helpText.setComponentClickHandler(this::handleComponentClicked);
        *///? }

        columns.addChild(this.helpText, rows.newCellSettings().paddingBottom(12));

        rows.addChild(Button.builder(Component.literal("<"), button ->
                pmState.cameraRotation.addToGoal(ROTATION_STEP_SIZE, this.getDeltaTicks()))
            .width(20)
            .build());
        rows.addChild(Button.builder(Component.translatable(TAKE_SCREENSHOT_KEY), button -> {
                    ScreenshotHandler screenshotHandler = PictureModeClient.getScreenshotHandler();
                    screenshotHandler.prepareForScreenshot(minecraft.gameDirectory, message ->
                        textDelegate = () -> {
                            helpText.setMessage(message);
                            repositionElements();
                        });
                })
            .width(98)
            .build());
        rows.addChild(Button.builder(Component.literal(">"), button ->
                pmState.cameraRotation.subtractFromGoal(ROTATION_STEP_SIZE, this.getDeltaTicks()))
            .width(20)
            .build());

        columns.addChild(actions);
        return layout;
    }

    @Override
    protected void repositionElements() {
        this.layout.updateDimensions(this.width, this.height);
        this.layout.arrangeElements();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, /*? >=1.20.2 {*/ double scrollX, /*?}*/ double scrollY) {
        if (scrollY < 0) {
            pmState.cameraZoom.subtractFromGoal(0.25D, this.getDeltaTicks());
        } else if (scrollY > 0) {
            pmState.cameraZoom.addToGoal(0.25D, this.getDeltaTicks());
        }

        return true;
    }

    @Override
    //? if >=1.21.9 {
    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double deltaX, double deltaY) {
        double mouseX = event.x();
        double mouseY = event.y();

        int button = event.button();
    //? } else {
    /*public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    *///? }

        float delta = this.getDeltaTicks();

        if (!super.mouseDragged(/*? >=1.21.9 {*/ event, /*? } else {*/ /*mouseX, mouseY, button, *//*?}*/ deltaX, deltaY)) {
            if (openedAtMillis + PANNING_INITIAL_DELAY_MS >= Util.getMillis())
                return true;

            Window window = minecraft.getWindow();
            mouseX *= (double) window.getScreenWidth() / window.getGuiScaledWidth();
            mouseY *= (double) window.getScreenHeight() / window.getGuiScaledHeight();

            if (button == 0) {
                double zoom = pmState.cameraZoom.getValue(this.getDeltaTicks());
                double div = Math.pow(2.0, zoom) / 3.0D;

                pmState.cameraPanX.setGoal(cameraPanXStart + (mouseX - mouseXStart) / div, delta);
                pmState.cameraPanY.setGoal(cameraPanYStart + (mouseY - mouseYStart) / div, delta);
            } else {
                pmState.cameraRotation.setGoal(cameraRotationStart + (mouseX - mouseXStart) * (ROTATION_STEP_SIZE / 128.0D), delta);
            }
        }
        return true;
    }

    @Override
    //? if >=1.21.9 {
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean isDoubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
    //? } else {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
    *///? }

        if (!super.mouseClicked(/*? >=1.21.9 {*/ event, isDoubleClick /*? } else {*/ /*mouseX, mouseY, button *//*?}*/)) {
            Window window = minecraft.getWindow();
            mouseX *= (double) window.getScreenWidth() / window.getGuiScaledWidth();
            mouseY *= (double) window.getScreenHeight() / window.getGuiScaledHeight();

            mouseXStart = mouseX;
            mouseYStart = mouseY;

            float delta = this.getDeltaTicks();

            cameraPanXStart = pmState.cameraPanX.getValue(delta);
            cameraPanYStart = pmState.cameraPanY.getValue(delta);
            cameraRotationStart = pmState.cameraRotation.getValue(delta);

            helpText.fadeOut();
        }
        return true;
    }

    @Override
    public void removed() {
        this.onExit();
    }

    @Override
    public void tick() {
        for (GuiEventListener eventListener : this.children()) {
            if (eventListener instanceof Tickable tickable) {
                tickable.onTick();
            }
        }
    }

    @Override
    public void onClose() {
        this.onExit();
        super.onClose();
    }

    private void onExit() {
        this.pmState.setEnabled(false);
    }

    private float getDeltaTicks() {
        //? if >=1.21 {
        return this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        //? } else {
        /*return this.minecraft.getFrameTime();
        *///? }
    }
}
