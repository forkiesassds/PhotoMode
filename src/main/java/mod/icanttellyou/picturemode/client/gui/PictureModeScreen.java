package mod.icanttellyou.picturemode.client.gui;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.gui.widget.Slider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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

    public PictureModeScreen(Component title) {
        super(title);
        this.pmState = PictureModeClient.getState();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    protected void init() {
        super.init();
        this.pmState.resetState();
        this.pmState.setEnabled(true);

        addRenderableWidget(new Slider(width - 150, -20, 0.0D,
            (slider, value, messageUpdate) -> {
                //TODO: time slider
            }));
        addRenderableWidget(new Slider(width - 150, 0, 1.0D,
            (slider, value, messageUpdate) -> {
                if (!messageUpdate) {
                    pmState.fog.setGoal(Math.pow(2.0D, 8.0D * value - 8.0D), this.getDeltaTicks());
                } else {
                    int percent = (int) (value * 100.0D);
                    slider.setMessage(Component.translatable(FOG_KEY, percent));
                }
            }));
        addRenderableWidget(new Slider(width - 150, 20, DEFAULT_TILT / TILT_ANGLES,
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

        addRenderableWidget(Button.builder(Component.literal("<"), (button) -> {
            pmState.cameraRotation.addToGoal(ROTATION_STEP_SIZE, this.getDeltaTicks());
        }).pos(width / 2 - 49 - 2 - 20, height - 20).width(20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), (button) -> {
            pmState.cameraRotation.subtractFromGoal(ROTATION_STEP_SIZE, this.getDeltaTicks());
        }).pos(width / 2 + 49 + 2, height - 20).width(20).build());
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
