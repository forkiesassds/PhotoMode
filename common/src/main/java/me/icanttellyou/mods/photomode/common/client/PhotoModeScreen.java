package me.icanttellyou.mods.photomode.common.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class PhotoModeScreen extends Screen {
    public boolean playerVisible = true;
    private float cameraRotation = 0.0f;
    private float cameraZoom = 1.0f;
    private float cameraTilt = 30.0f;
    private float cameraFog = 1.0f;
    private float cametaPanX = 0.0f;
    private float cametaPanY = 0.0f;
    private float lastCameraRotation = 0.0f;
    private float lastCameraZoom = 1.0f;
    private float lastCameraTilt = 30.0f;
    private float lastCameraFog = 1.0f;
    private float lastCametaPanX = 0.0f;
    private float lastCametaPanY = 0.0f;
    private float cameraRotationGoal = 0.0f;
    private float cameraZoomGoal = 1.0f;
    private float cameraTiltGoal = 30.0f;
    private float cameraFogGoal = 1.0f;
    private float cametaPanXGoal = 0.0f;
    private float cametaPanYGoal = 0.0f;
    private float lastPanXEnd;
    private float lastPanYEnd;
    private float lastRotationEnd;
    private long lastGuiUpdateTime = 0L;
    private double initMouseX;
    private double initMouseY;
    private long oldTime;
    private long selectedTime = -1L;
    private long selectedDay = -1L;
    private boolean showInfoText = true;
    private boolean isTakingScreenshot = false;
    private final boolean wasHudHidden = MinecraftClient.getInstance().options.hudHidden;
    private final boolean wasChunkCullingEnabled = MinecraftClient.getInstance().chunkCullingEnabled;

    ButtonWidget centerScreen;
    ButtonWidget showPlayer;
    PhotoModeSliderWidget tiltSlider;
    PhotoModeSliderWidget timeSlider;
    PhotoModeSliderWidget fogSlider;

    public PhotoModeScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        initWidgets();
        updateGui();
        MinecraftClient.getInstance().options.hudHidden = true;
        MinecraftClient.getInstance().chunkCullingEnabled = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        if (isTakingScreenshot) {
            ScreenshotRecorder.saveScreenshot(client.runDirectory, client.getFramebuffer(), text -> {});
            isTakingScreenshot = false;
        } else {
            super.render(matrices, mouseX, mouseY, delta);
            if (tiltSlider.isDragging) {
                cameraTiltGoal = (float) (int)(tiltSlider.value * 90.0f);
            }

            if (fogSlider.isDragging) {
                cameraFogGoal = (float)Math.pow(2.0, 8.0f * fogSlider.value - 8.0f);
            }

            if (timeSlider.isDragging) {
                long time = (long)(timeSlider.value * 24000.0f);
                selectedTime = timeSlider.value == 0.0f ? oldTime % 24000L : time;
                assert client.world != null;
                client.world.setTimeOfDay(selectedDay + selectedTime);
                client.gameRenderer.tick();
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime > lastGuiUpdateTime + 5L) {
                lastCameraRotation = cameraRotation;
                if (cameraRotation != cameraRotationGoal) {
                    cameraRotation += (cameraRotationGoal - cameraRotation) * 0.08F;
                    if (Math.abs(cameraRotation - cameraRotationGoal) < 5.0E-4f) {
                        cameraRotation = cameraRotationGoal;
                    }
                }

                lastCameraTilt = cameraTilt;
                if (cameraTilt != cameraTiltGoal) {
                    cameraTilt += (cameraTiltGoal - cameraTilt) * 0.08F;
                    if (Math.abs(cameraTilt - cameraTiltGoal) < 0.01f) {
                        cameraTilt = cameraTiltGoal;
                    }
                }

                lastCameraZoom = cameraZoom;
                if (cameraZoom != cameraZoomGoal) {
                    cameraZoom += (cameraZoomGoal - cameraZoom) * 0.08F;
                    if (Math.abs(cameraZoom - cameraZoomGoal) < 5.0E-4f) {
                        cameraZoom = cameraZoomGoal;
                    }
                }

                lastCameraFog = lastCameraZoom;
                //FIXME: change this to be more like the other goal updaters as soon as i get tick delta working in rendersystem
                if (cameraFog != cameraFogGoal) {
                    cameraFog += (cameraFogGoal - cameraFog) * 0.02f + (cameraFogGoal - cameraFog) * 0.02f * delta;
                    if (Math.abs(cameraFog - cameraFogGoal) < 5.0E-5f) {
                        cameraFog = cameraFogGoal;
                    }
                }

                lastCametaPanX = cametaPanX;
                if (cametaPanX != cametaPanXGoal) {
                    cametaPanX += (cametaPanXGoal - cametaPanX) * 0.4F;
                    if (Math.abs(cametaPanX - cametaPanXGoal) < 0.01F) {
                        cametaPanX = cametaPanXGoal;
                    }
                }

                lastCametaPanY = cametaPanY;
                if (cametaPanY != cametaPanYGoal) {
                    cametaPanY += (cametaPanYGoal - cametaPanY) * 0.4F;
                    if (Math.abs(cametaPanY - cametaPanYGoal) < 0.01F) {
                        cametaPanY = cametaPanYGoal;
                    }
                }

                lastGuiUpdateTime = currentTime;
            }

            if (showInfoText) {
                drawCenteredTextWithShadow(matrices, textRenderer, Text.translatable("gui.photomode.helpText").asOrderedText(), width / 2, height - 56, 0xFFFFFF);
            }
        }
        updateGui();
    }

    private void initWidgets() {
        addDrawableChild(centerScreen = ButtonWidget.builder(Text.translatable("gui.photomode.centerScreen"), (button) -> {
            cametaPanXGoal = 0.0F;
            cametaPanYGoal = 0.0F;
            cameraRotationGoal = 0.0F;
        }).position(width - 150, 0).build());

        addDrawableChild(showPlayer = ButtonWidget.builder(Text.translatable("gui.photomode.showPlayer", ScreenTexts.ON), (button) ->
                playerVisible = !playerVisible).position(width - 150, 0).build());

        timeSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Text.translatable("gui.photomode.time"), 0.0f);
        fogSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Text.translatable("gui.photomode.fog"), 1.0f);
        tiltSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Text.translatable("gui.photomode.tilt"), 0.33333334f);
        addDrawableChild(tiltSlider);
        assert client != null;
        if (client.isInSingleplayer()) {
            addDrawableChild(timeSlider);
            addDrawableChild(fogSlider);
        }

        int i = 0;
        for (Object button : children()) {
            ((ClickableWidget)button).setPos(((ClickableWidget) button).getX(), i++ * 21);
        }


        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.photomode.takescreenshot"), (button) ->
                isTakingScreenshot = true).position(width / 2 - 49, height - 20).width(98).build());

        addDrawableChild(ButtonWidget.builder(Text.of("X"), (button) ->
                client.setScreen(new GameMenuScreen(true))).position(0, 0).width(20).build());

        addDrawableChild(ButtonWidget.builder(Text.of("<"), (button) -> {
            cameraRotationGoal--;
            cameraRotationGoal = (int)cameraRotationGoal;
        }).position(width / 2 - 49 - 2 - 20, height - 20).width(20).build());
        addDrawableChild(ButtonWidget.builder(Text.of(">"), (button) -> {
            cameraRotationGoal++;
            cameraRotationGoal = (int)cameraRotationGoal;
        }).position(width / 2 + 49 + 2, height - 20).width(20).build());

        assert client.world != null;
        oldTime = client.world.getTimeOfDay();
        if (timeSlider.value != 0.0F) {
            if (selectedTime == -1L) {
                selectedTime = oldTime % 24000L;
            } else {
                timeSlider.value = (float) selectedTime / 24000.0f;
            }
            if (selectedDay == -1L) {
                selectedDay = oldTime / 24000L;
            }
        }
    }

    private void updateGui() {
        timeSlider.setText(Text.translatable("gui.photomode.time", timeSlider.value == 0.0f ? Text.translatable("gui.photomode.default") : (long)(timeSlider.value * 24000.0f)));
        fogSlider.setText(Text.translatable("gui.photomode.fog", (int)(fogSlider.value * 100.0f)));
        tiltSlider.setText(Text.translatable("gui.photomode.tilt", (int)(tiltSlider.value * 90.0f) == 30 ? Text.translatable("gui.photomode.default") : (int)(tiltSlider.value * 90.0f)).append(" ").append((int)(tiltSlider.value * 90.0f) == 30 ? Text.of("") : Text.translatable("gui.photomode.degrees")));
        showPlayer.setMessage(Text.translatable("gui.photomode.showPlayer", ScreenTexts.onOrOff(playerVisible)));
        centerScreen.active = (cametaPanX != 0.0F || cametaPanY != 0.0F || cameraRotation != 0.0F) && (cametaPanXGoal != 0.0F || cametaPanYGoal != 0.0F || cameraRotationGoal != 0.0F);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount < 0) {
            cameraZoomGoal -= 0.25f;
        } else if (amount > 0) {
            cameraZoomGoal += 0.25f;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) && !showInfoText) {
            if (button == 0) {
                assert client != null;
                float div = (float) Math.pow(2.0, cameraZoom) / client.options.getGuiScale().getValue();
                cametaPanXGoal = lastPanXEnd + (float) (mouseX - initMouseX) / div;
                cametaPanYGoal = lastPanYEnd + (float) (mouseY - initMouseY) / div;
            } else {
                cameraRotationGoal = lastRotationEnd + (float) (mouseX - initMouseX) / 128.0F;
            }
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            showInfoText = false;
            initMouseX = mouseX;
            initMouseY = mouseY;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!super.mouseReleased(mouseX, mouseY, button)) {
            lastPanXEnd = cametaPanX;
            lastPanYEnd = cametaPanY;
            lastRotationEnd = cameraRotation;
        }
        return true;
    }

    public float getRotation(float delta) {
        return lastCameraRotation + (cameraRotation - lastCameraRotation) * delta;
    }

    public float getZoom(float delta) {
        return lastCameraZoom + (cameraZoom - lastCameraZoom) * delta;
    }

    public float getTilt(float delta) {
        return lastCameraTilt + (cameraTilt - lastCameraTilt) * delta;
    }

    //FIXME: can't seem to get the tick delta working in the way i have set up my mixins
    public float getFog() {
        return lastCameraFog + (cameraFog - lastCameraFog);
    }

    public float getPanX(float delta) {
        return lastCametaPanX + (cametaPanX - lastCametaPanX) * delta;
    }

    public float getPanY(float delta) {
        return lastCametaPanY + (cametaPanY - lastCametaPanY) * delta;
    }

    @Override
    public void close() {
        super.close();
        assert client != null;
        assert client.world != null;
        client.world.setTimeOfDay(oldTime);
        MinecraftClient.getInstance().options.hudHidden = wasHudHidden;
        MinecraftClient.getInstance().chunkCullingEnabled = wasChunkCullingEnabled;
    }
}

