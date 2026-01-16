package mod.icanttellyou.picturemode.client.gui;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.gui.layout.AnchorLayout;
import mod.icanttellyou.picturemode.client.gui.widget.Slider;
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

    private final PictureModeState pmState;
    private final AnchorLayout layout;

    public PictureModeScreen(Component title) {
        super(title);
        this.pmState = PictureModeClient.getState();
        this.layout = new AnchorLayout(0, 0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics graphics /*? >=1.20.2 {*/, int mouseX, int mouseY, float partialTick/*?}*/) {}

    @Override
    protected void init() {
        super.init();
        this.pmState.resetState();
        this.pmState.setEnabled(true);

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

        rows.addChild(new Slider(0, 0, 0.0D,
            (slider, value, messageUpdate) -> {
                //TODO: time slider
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

        return layout;
    }

    private GridLayout makeActions() {
        GridLayout layout = new GridLayout();
        GridLayout.RowHelper rows = layout.createRowHelper(3);

        rows.addChild(Button.builder(Component.literal("<"), button ->
                pmState.cameraRotation.addToGoal(ROTATION_STEP_SIZE, this.getDeltaTicks()))
            .width(20)
            .build());
        rows.addChild(Button.builder(Component.literal(">"), (button) ->
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
