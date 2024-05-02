package me.icanttellyou.mods.photomode.common.client;

import me.icanttellyou.mods.photomode.common.mixin.AccessGameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class PhotoModeScreen extends Screen {
    public boolean playerVisible = true;
    private float cameraRotation = 0.0f;
    private float cameraZoom = 1.0f;
    private float cameraTilt = 30.0f;
    private float cameraFog = 1.0f;
    private float cameraPanX = 0.0f;
    private float cameraPanY = 0.0f;
    private float lastCameraRotation = 0.0f;
    private float lastCameraZoom = 1.0f;
    private float lastCameraTilt = 30.0f;
    private float lastCameraFog = 1.0f;
    private float lastCameraPanX = 0.0f;
    private float lastCameraPanY = 0.0f;
    private float cameraRotationGoal = 0.0f;
    private float cameraZoomGoal = 1.0f;
    private float cameraTiltGoal = 30.0f;
    private float cameraFogGoal = 1.0f;
    private float cameraPanXGoal = 0.0f;
    private float cameraPanYGoal = 0.0f;
    private float lastPanXEnd;
    private float lastPanYEnd;
    private float lastRotationEnd;
    public float shaderIntensity = 1.0F;
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

    private int currentShader = -1;

    ButtonWidget centerScreen;
    ButtonWidget showPlayer;
    ButtonWidget shader;
    PhotoModeSliderWidget tiltSlider;
    PhotoModeSliderWidget timeSlider;
    PhotoModeSliderWidget fogSlider;
    PhotoModeSliderWidget intensitySlider;

    public PhotoModeScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        initWidgets();
        updateGui();

        assert client != null;

        client.options.hudHidden = true;
        client.chunkCullingEnabled = false;
        client.gameRenderer.setRenderHand(false);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        assert client != null;
        if (isTakingScreenshot) {
            ScreenshotRecorder.saveScreenshot(client.runDirectory, client.getFramebuffer(), text -> {});
            isTakingScreenshot = false;
        } else {
            super.render(drawContext, mouseX, mouseY, delta);
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

            if (intensitySlider.isDragging) {
                shaderIntensity = (float) intensitySlider.value;
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

                lastCameraFog = cameraFog;
                if (cameraFog != cameraFogGoal) {
                    cameraFog += (cameraFogGoal - cameraFog) * 0.02f;
                    if (Math.abs(cameraFog - cameraFogGoal) < 5.0E-5f) {
                        cameraFog = cameraFogGoal;
                    }
                }

                lastCameraPanX = cameraPanX;
                if (cameraPanX != cameraPanXGoal) {
                    cameraPanX += (cameraPanXGoal - cameraPanX) * 0.4F;
                    if (Math.abs(cameraPanX - cameraPanXGoal) < 0.01F) {
                        cameraPanX = cameraPanXGoal;
                    }
                }

                lastCameraPanY = cameraPanY;
                if (cameraPanY != cameraPanYGoal) {
                    cameraPanY += (cameraPanYGoal - cameraPanY) * 0.4F;
                    if (Math.abs(cameraPanY - cameraPanYGoal) < 0.01F) {
                        cameraPanY = cameraPanYGoal;
                    }
                }

                lastGuiUpdateTime = currentTime;
            }

            if (showInfoText) {
                drawContext.drawCenteredTextWithShadow(textRenderer, Text.translatable("gui.photomode.helpText"), width / 2, height - 56, 0xFFFFFF);
            }
        }
        updateGui();
    }

    public void cycleShader() {
        assert client != null;

        if (client.getCameraEntity() instanceof PlayerEntity) {
            GameRenderer gr = client.gameRenderer;

            if (gr.getPostProcessor() != null) {
                gr.getPostProcessor().close();
            }

            currentShader = (currentShader + 1) % (PhotoModeUtils.SHADER_PROGRAM_COUNT + 1);
            if (hasControlDown() || currentShader == PhotoModeUtils.SHADER_PROGRAM_COUNT) {
                ((AccessGameRenderer) gr).photoMode$setPostProcessor(null);
                currentShader = -1;
            } else {
                PhotoModeUtils.loadPMPostProcessor(this, gr, PhotoModeUtils.SHADER_PROGRAMS[currentShader]);
            }
        }
    }

    private void initWidgets() {
        addDrawableChild(centerScreen = ButtonWidget.builder(Text.translatable("gui.photomode.centerCamera"), (button) -> {
            cameraPanXGoal = 0.0F;
            cameraPanYGoal = 0.0F;
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

        addDrawableChild(shader = ButtonWidget.builder(Text.translatable("gui.photomode.shader", Text.translatable("gui.photomode.none")), (button) -> {
            cycleShader();
            this.intensitySlider.active = client.gameRenderer.getPostProcessor() != null;
        }).position(width - 150, 0).build());

        intensitySlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Text.translatable("gui.photomode.intensity"), shaderIntensity);
        intensitySlider.active = client.gameRenderer.getPostProcessor() != null;
        addDrawableChild(intensitySlider);

        int i = 0;
        for (Object button : children()) {
            ((ClickableWidget)button).setPosition(((ClickableWidget) button).getX(), i++ * 21);
        }


        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.photomode.takescreenshot"), (button) ->
                isTakingScreenshot = true).position(width / 2 - 49, height - 20).width(98).build());

        addDrawableChild(ButtonWidget.builder(Text.of("X"), (button) -> {
            onPhotoModeClose();
            client.setScreen(new GameMenuScreen(true));
        }).position(0, 0).width(20).build());

        addDrawableChild(ButtonWidget.builder(Text.of("<"), (button) -> {
            cameraRotationGoal++;
            cameraRotationGoal = (int)cameraRotationGoal;
        }).position(width / 2 - 49 - 2 - 20, height - 20).width(20).build());
        addDrawableChild(ButtonWidget.builder(Text.of(">"), (button) -> {
            cameraRotationGoal--;
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
        tiltSlider.setText(Text.translatable("gui.photomode.tilt", (int)(tiltSlider.value * 90.0f) == 30 ? Text.translatable("gui.photomode.default") : (int)(tiltSlider.value * 90.0f)).append(ScreenTexts.SPACE).append((int)(tiltSlider.value * 90.0f) == 30 ? ScreenTexts.EMPTY : Text.translatable("gui.photomode.degrees")));
        showPlayer.setMessage(Text.translatable("gui.photomode.showPlayer", ScreenTexts.onOrOff(playerVisible)));

        assert client != null;
        PostEffectProcessor postEffectProcessor = client.gameRenderer.getPostProcessor();
        Text shaderName = Text.translatable("gui.photomode.none");
        if (postEffectProcessor != null) {
            String[] splitPath = postEffectProcessor.getName().split("/");

            String name = splitPath[splitPath.length - 1];
            name = name.substring(0, name.indexOf("."));
            shaderName = Text.translatable("photomode.shader." + name);
        }

        shader.setMessage(Text.translatable("gui.photomode.shader", shaderName));
        intensitySlider.setText(Text.translatable("gui.photomode.intensity", (int)(intensitySlider.value * 100.0F)));

        centerScreen.active = (cameraPanX != 0.0F || cameraPanY != 0.0F || cameraRotation != 0.0F) && (cameraPanXGoal != 0.0F || cameraPanYGoal != 0.0F || cameraRotationGoal != 0.0F);
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
                cameraPanXGoal = lastPanXEnd + (float) (mouseX - initMouseX) / div;
                cameraPanYGoal = lastPanYEnd + (float) (mouseY - initMouseY) / div;
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
            lastPanXEnd = cameraPanX;
            lastPanYEnd = cameraPanY;
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

    public float getFog(float delta) {
        return lastCameraFog + (cameraFog - lastCameraFog) * delta;
    }

    public float getPanX(float delta) {
        return lastCameraPanX + (cameraPanX - lastCameraPanX) * delta;
    }

    public float getPanY(float delta) {
        return lastCameraPanY + (cameraPanY - lastCameraPanY) * delta;
    }

    @Override
    public void close() {
        super.close();
        onPhotoModeClose();
    }

    private void onPhotoModeClose() {
        assert client != null;
        assert client.world != null;

        client.world.setTimeOfDay(oldTime);
        client.options.hudHidden = wasHudHidden;
        client.chunkCullingEnabled = wasChunkCullingEnabled;
        client.gameRenderer.disablePostProcessor();
        client.gameRenderer.setRenderHand(true);
    }
}

