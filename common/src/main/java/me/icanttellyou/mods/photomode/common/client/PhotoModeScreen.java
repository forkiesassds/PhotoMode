package me.icanttellyou.mods.photomode.common.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PhotoModeScreen extends Screen {
    public float cameraRotation = 0.0f;
    public float cameraZoom = 1.0f;
    public float cameraTilt = 30.0f;
    public float cameraFog = 1.0f;
    private float cameraRotationGoal = 0.0f;
    private float cameraZoomGoal = 1.0f;
    private float cameraTiltGoal = 30.0f;
    private float cameraFogGoal = 1.0f;
    private long oldTime;
    private long selectedTime = -1L;
    private long selectedDay = -1L;
    private boolean isTakingScreenshot = false;
    private final boolean wasHudHidden = MinecraftClient.getInstance().options.hudHidden;

    PhotoModeSliderWidget tiltSlider;
    PhotoModeSliderWidget timeSlider;
    PhotoModeSliderWidget fogSlider;

    public PhotoModeScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.initWidgets();
        this.updateGui();
        MinecraftClient.getInstance().options.hudHidden = true;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (isTakingScreenshot) {
            ScreenshotRecorder.saveScreenshot(client.runDirectory, this.client.getFramebuffer(), text -> {});
            isTakingScreenshot = false;
        } else {
            super.render(matrices, mouseX, mouseY, delta);
            if (tiltSlider.isDragging) {
                float cameraAngle = (int)(tiltSlider.value * 90.0f);
                cameraTiltGoal = cameraAngle;
            }

            if (fogSlider.isDragging) {
                this.cameraFogGoal = (float)Math.pow(2.0, 8.0f * this.fogSlider.value - 8.0f);
            }

            if (timeSlider.isDragging) {
                long time = (long)(this.timeSlider.value * 24000.0f);
                this.selectedTime = this.timeSlider.value == 0.0f ? this.oldTime % 24000L : time;
                this.client.world.setTimeOfDay(this.selectedDay + this.selectedTime);
                this.client.gameRenderer.tick();
            }

            if (this.cameraRotation != this.cameraRotationGoal) {
                this.cameraRotation += (this.cameraRotationGoal - this.cameraRotation) * 0.02f + (this.cameraRotationGoal - this.cameraRotation) * 0.02f * delta;
                if (Math.abs(this.cameraRotation - this.cameraRotationGoal) < 5.0E-4f) {
                    this.cameraRotation = this.cameraRotationGoal;
                }
            }

            if (this.cameraTilt != this.cameraTiltGoal) {
                this.cameraTilt += (this.cameraTiltGoal - this.cameraTilt) * 0.02f + (this.cameraTiltGoal - this.cameraTilt) * 0.02f * delta;
                if (Math.abs(this.cameraTilt - this.cameraTiltGoal) < 0.01f) {
                    this.cameraTilt = this.cameraTiltGoal;
                }
            }

            if (this.cameraZoom != this.cameraZoomGoal) {
                this.cameraZoom += (this.cameraZoomGoal - this.cameraZoom) * 0.02f + (this.cameraZoomGoal - this.cameraZoom) * 0.02f * delta;
                if (Math.abs(this.cameraZoom - this.cameraZoomGoal) < 5.0E-4f) {
                    this.cameraZoom = this.cameraZoomGoal;
                }
            }

            if (this.cameraFog != this.cameraFogGoal) {
                this.cameraFog += (this.cameraFogGoal - this.cameraFog) * 0.02f + (this.cameraFogGoal - this.cameraFog) * 0.02f * delta;
                if (Math.abs(this.cameraFog - this.cameraFogGoal) < 5.0E-5f) {
                    this.cameraFog = this.cameraFogGoal;
                }
            }
        }
        updateGui();
    }

    private void initWidgets() {
        timeSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, new TranslatableText("gui.photomode.time"), 0.0f);
        fogSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, new TranslatableText("gui.photomode.fog"), 1.0f);
        tiltSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, new TranslatableText("gui.photomode.tilt"), 0.33333334f);
        this.addDrawableChild(tiltSlider);
        if (this.client.isInSingleplayer()) {
            this.addDrawableChild(timeSlider);
            this.addDrawableChild(fogSlider);
        }

        int i = 0;
        for (Object button : this.children()) {
            ((ClickableWidget)button).y = i++ * 20;
        }

        this.addDrawableChild(new ButtonWidget(width / 2 - 49, height - 20, 98, 20, new TranslatableText("gui.photomode.takescreenshot"), (button) -> {
            isTakingScreenshot = true;
        }));
        this.addDrawableChild(new ButtonWidget(0, 0, 20, 20, Text.of("X"), (button) -> {
            client.setScreen(new GameMenuScreen(true));
        }));
        this.addDrawableChild(new ButtonWidget(width / 2 - 49 - 2 - 20, height - 20, 20, 20, Text.of("<"), (button) -> {
            this.cameraRotationGoal -= 1.0f;
            this.cameraRotationGoal = (int)this.cameraRotationGoal;
        }));
        this.addDrawableChild(new ButtonWidget(width / 2 + 49 + 2, height - 20, 20, 20, Text.of(">"), (button) -> {
            this.cameraRotationGoal += 1.0f;
            this.cameraRotationGoal = (int)this.cameraRotationGoal;
        }));

        this.oldTime = this.client.world.getTimeOfDay();
        if (timeSlider.value != 0.0F) {
            if (this.selectedTime == -1L) {
                this.selectedTime = this.oldTime % 24000L;
            } else {
                this.timeSlider.value = (float) this.selectedTime / 24000.0f;
            }
            if (this.selectedDay == -1L) {
                this.selectedDay = this.oldTime / 24000L;
            }
        }
    }

    private void updateGui() {
        this.timeSlider.setText(new TranslatableText("gui.photomode.time", this.timeSlider.value == 0.0f ? new TranslatableText("gui.photomode.default") : (long)(this.timeSlider.value * 24000.0f)));
        this.fogSlider.setText(new TranslatableText("gui.photomode.fog", (int)(this.fogSlider.value * 100.0f)));
        this.tiltSlider.setText(new TranslatableText("gui.photomode.tilt", (int)(this.tiltSlider.value * 90.0f) == 30 ? new TranslatableText("gui.photomode.default") : (int)(this.tiltSlider.value * 90.0f)).append(" ").append((int)(this.tiltSlider.value * 90.0f) == 30 ? Text.of("") : new TranslatableText("gui.photomode.degrees")));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount < 0) {
            this.cameraZoomGoal -= 0.25f;
        } else if (amount > 0) {
            this.cameraZoomGoal += 0.25f;
        }
        return true;
    }

    @Override
    public void close() {
        super.close();
        this.client.world.setTimeOfDay(this.oldTime);
        MinecraftClient.getInstance().options.hudHidden = wasHudHidden;
    }
}

