package mod.icanttellyou.photomode.client;

import mod.icanttellyou.photomode.value.InterpolatedValue;
import mod.icanttellyou.photomode.value.Value;

public class PhotoModeState {
    private boolean enabled = false;

    private Value cameraRotation = new InterpolatedValue(13);
    private Value cameraTilt = new InterpolatedValue(30.0D, 13);
    private Value cameraZoom = new InterpolatedValue(1.0D, 13);
    private Value fog = new InterpolatedValue(1.0D, 50);
    private Value cameraPanX = new InterpolatedValue(3);
    private Value cameraPanY = new InterpolatedValue(3);

}
