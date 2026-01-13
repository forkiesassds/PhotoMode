package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.value.Easing;
import mod.icanttellyou.picturemode.value.InterpolatedValue;
import mod.icanttellyou.picturemode.value.StaticValue;
import mod.icanttellyou.picturemode.value.Value;
import org.joml.Matrix4f;

public class PictureModeState {
    private static final double DEFAULT_ROTATION = 45.0D;
    private static final double DEFAULT_TILT = 30.0D;

    private boolean enabled = false;

    private final Value cameraRotation = new InterpolatedValue(Easing.LINEAR, DEFAULT_ROTATION, 12.5D);
    private final Value cameraTilt = new InterpolatedValue(Easing.LINEAR, DEFAULT_TILT, 12.5D);
    private final Value cameraZoom = new InterpolatedValue(Easing.LINEAR, 1.0D, 12.5D);
    private final Value fog = new InterpolatedValue(Easing.LINEAR, 1.0D, 50.0D);
    private final Value cameraPanX = new InterpolatedValue(Easing.LINEAR, 2.5D);
    private final Value cameraPanY = new InterpolatedValue(Easing.LINEAR, 2.5D);
    private final Value shaderIntensity = new StaticValue(1.0D);

    /**
     * Gets the projection matrix for the Picture Mode state
     *
     * @param width    The viewport width
     * @param height   The viewport height
     * @param farPlane The far plane distance
     * @param delta    The delta time for getting values
     * @return A {@link Matrix4f} with an orthographic projection matrix.
     */
    public Matrix4f getProjectionMatrix(int width, int height, float farPlane, double delta) {
        float viewWidth = (float) adjustViewportDimension(width, delta);
        float viewHeight = (float) adjustViewportDimension(height, delta);

        float panX = (float) cameraPanX.getValue(delta);
        float panY = (float) cameraPanY.getValue(delta);

        return new Matrix4f()
            .setOrtho(-viewWidth, viewWidth, -viewHeight, viewHeight, -farPlane * 2.0F, farPlane * 2.0F)
            .translate(panX, -panY, 0.0F);
    }

    /**
     * Adjusts a dimension (width, height) with zoom, based on the current state
     *
     * @param baseDimension The base dimension to adjust
     * @param delta         The delta time for zoom values
     * @return The dimension, adjusted to have the zoom applied
     */
    public double adjustViewportDimension(int baseDimension, double delta) {
        float zoom = (float) cameraZoom.getValue(delta);
        float sqZoom = zoom * zoom;

        return baseDimension / sqZoom;
    }

    /**
     * Sets up camera angles for the Picture Mode state
     *
     * @param delta       The delta time for getting values
     * @param angleSetter The callback for setting the angle
     */
    public void setupCameraAngles(double delta, AngleSetter angleSetter) {
        float yaw = (float) cameraRotation.getValue(delta);
        float pitch = (float) cameraTilt.getValue(delta);

        angleSetter.setAngles(yaw, pitch);
    }

    /**
     * Resets the Picture Mode state back to defaults
     */
    public void resetState() {
        cameraRotation.setValue(DEFAULT_ROTATION);
        cameraTilt.setValue(DEFAULT_TILT);
        cameraZoom.setValue(1.0D);
        fog.setValue(1.0D);
        cameraPanX.setValue(0.0D);
        cameraPanY.setValue(0.0D);
        shaderIntensity.setValue(1.0D);
    }

    /**
     * Is the Picture Mode state enabled or not.
     *
     * @return Whether the Picture Mode state is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if the Picture Mode state is enabled or not
     *
     * @param enabled Should the state should be enabled or not
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * A callback for setting camera angles
     */
    @FunctionalInterface
    public interface AngleSetter {
        /**
         * This method is responsible for setting the camera angle
         *
         * @param yaw   The yaw angle to be set
         * @param pitch The pitch angle to be set
         */
        void setAngles(float yaw, float pitch);
    }
}
