package mod.icanttellyou.picturemode.client.gui;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.gui.layout.AnchorLayout;
import mod.icanttellyou.picturemode.client.gui.widget.Slider;
import mod.icanttellyou.picturemode.util.LevelUtils;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static mod.icanttellyou.picturemode.PictureModeConstants.*;

public class PictureModeScreen extends Screen {
    private static final String DEFAULT_KEY = "gui.picturemode.default";
    private static final String DEGREES_KEY = "gui.picturemode.degrees";

    private static final String TIME_KEY = "gui.picturemode.time";
    private static final String FOG_KEY = "gui.picturemode.fog";
    private static final String TILT_KEY = "gui.picturemode.tilt";

    private static final String TAKE_SCREENSHOT_KEY = "gui.picturemode.takeScreenshot";

    private boolean isTakingScreenshot;

    private final Screen parent;
    private final PictureModeState pmState;
    private final AnchorLayout layout;

    public PictureModeScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        this.pmState = PictureModeClient.getState();
        this.layout = new AnchorLayout(0, 0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!isTakingScreenshot) {
            super.render(graphics, mouseX, mouseY, partialTick);
        } else {
            Screenshot.grab(minecraft.gameDirectory, minecraft.getMainRenderTarget(), message -> {
                //TODO: help text display
            });
            isTakingScreenshot = false;
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics /*? >=1.20.2 {*/, int mouseX, int mouseY, float partialTick/*?}*/) {}

    @Override
    protected void init() {
        super.init();
        this.pmState.resetState();
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

        return layout;
    }

    private GridLayout makeActions() {
        GridLayout layout = new GridLayout();
        layout.defaultCellSetting()
            .alignHorizontallyCenter()
            .paddingHorizontal(1);

        GridLayout.RowHelper rows = layout.createRowHelper(3);

        rows.addChild(Button.builder(Component.literal("<"), button ->
                pmState.cameraRotation.addToGoal(ROTATION_STEP_SIZE, this.getDeltaTicks()))
            .width(20)
            .build());
        rows.addChild(Button.builder(Component.translatable(TAKE_SCREENSHOT_KEY), button ->
                this.isTakingScreenshot = true)
            .width(98)
            .build());
        rows.addChild(Button.builder(Component.literal(">"), button ->
                pmState.cameraRotation.subtractFromGoal(ROTATION_STEP_SIZE, this.getDeltaTicks()))
            .width(20)
            .build());

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
    public void removed() {
        this.pmState.setEnabled(false);
    }

    @Override
    public void onClose() {
        this.pmState.setEnabled(false);
        super.onClose();
    }

    private float getDeltaTicks() {
        //? if >=1.21 {
        return this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        //? } else {
        /*return this.minecraft.getFrameTime();
        *///? }
    }
}
