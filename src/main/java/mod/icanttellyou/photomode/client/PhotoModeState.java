package mod.icanttellyou.photomode.client;

import mod.icanttellyou.photomode.value.Easing;
import mod.icanttellyou.photomode.value.InterpolatedValue;
import mod.icanttellyou.photomode.value.Value;

public class PhotoModeState {
    private boolean enabled = false;

    private final Value cameraRotation = new InterpolatedValue(Easing.LINEAR, 12.5D);
    private final Value cameraTilt = new InterpolatedValue(Easing.LINEAR, 30.0D, 12.5D);
    private final Value cameraZoom = new InterpolatedValue(Easing.LINEAR, 1.0D, 12.5D);
    private final Value fog = new InterpolatedValue(Easing.LINEAR, 1.0D, 50.0D);
    private final Value cameraPanX = new InterpolatedValue(Easing.LINEAR, 2.5D);
    private final Value cameraPanY = new InterpolatedValue(Easing.LINEAR, 2.5D);

}
