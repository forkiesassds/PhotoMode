package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.value.Easing;
import mod.icanttellyou.picturemode.value.InterpolatedValue;
import mod.icanttellyou.picturemode.value.Value;

public class PictureModeState {
    private boolean enabled = false;

    private final Value cameraRotation = new InterpolatedValue(Easing.LINEAR, 12.5D);
    private final Value cameraTilt = new InterpolatedValue(Easing.LINEAR, 30.0D, 12.5D);
    private final Value cameraZoom = new InterpolatedValue(Easing.LINEAR, 1.0D, 12.5D);
    private final Value fog = new InterpolatedValue(Easing.LINEAR, 1.0D, 50.0D);
    private final Value cameraPanX = new InterpolatedValue(Easing.LINEAR, 2.5D);
    private final Value cameraPanY = new InterpolatedValue(Easing.LINEAR, 2.5D);

}
