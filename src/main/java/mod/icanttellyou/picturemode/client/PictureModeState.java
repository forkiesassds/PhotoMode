package mod.icanttellyou.picturemode.client;

import mod.icanttellyou.picturemode.PictureModeConstants;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import mod.icanttellyou.picturemode.util.Tickable;
import mod.icanttellyou.picturemode.value.Easing;
import mod.icanttellyou.picturemode.value.InterpolatedValue;
import mod.icanttellyou.picturemode.value.StaticValue;
import mod.icanttellyou.picturemode.value.Value;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.slf4j.event.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PictureModeState {
    private boolean enabled = false;

    public final Value cameraRotation = new InterpolatedValue(Easing.EXPONENTIAL, PictureModeConstants.DEFAULT_ROTATION, 25.0D);
    public final Value cameraTilt = new InterpolatedValue(Easing.EXPONENTIAL, PictureModeConstants.DEFAULT_TILT, 25.0D);
    public final Value cameraZoom = new InterpolatedValue(Easing.EXPONENTIAL, 1.0D, 25.0D).clamped(0.0D, Double.MAX_VALUE);
    public final Value fog = new InterpolatedValue(Easing.EXPONENTIAL, 1.0D, 100.0D);
    public final Value cameraPanX = new InterpolatedValue(Easing.EXPONENTIAL, 5.0D);
    public final Value cameraPanY = new InterpolatedValue(Easing.EXPONENTIAL, 5.0D);
//    public final Value shaderIntensity = new StaticValue(1.0D);
    public final Value timeOverride = new StaticValue();

    private boolean showPlayer = true;

    private final List<Tickable> tickingCallbacks = new ArrayList<>();

    public PictureModeState() {
        addTickableCallbacks(getClass(), this);
    }

    /**
     * Gets the projection matrix for the Picture Mode state
     *
     * @param width     The viewport width
     * @param height    The viewport height
     * @param farPlane  The far plane distance
     * @param delta     The delta time for getting values
     * @return A {@link Matrix4f} with an orthographic projection matrix.
     */
    public Matrix4f getProjectionMatrix(int width, int height, float farPlane, double delta) {
        float viewWidth = (float) adjustViewportDimension(width, delta);
        float viewHeight = (float) adjustViewportDimension(height, delta);

        float panX = (float) cameraPanX.getValue(delta);
        float panY = (float) cameraPanY.getValue(delta);

        if (Float.isInfinite(farPlane)) {
            farPlane = 9999.0F;
        }

        return new Matrix4f().setOrtho(
                -viewWidth, viewWidth,
                -viewHeight, viewHeight,
                -farPlane * 2.0F, farPlane * 2.0F
                //? if >=26.1
                //, com.mojang.blaze3d.systems.RenderSystem.getDevice().isZZeroToOne()
            )
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
        double zoom = cameraZoom.getValue(delta);
        double expZoom = Math.pow(2.0D, zoom);

        return baseDimension / expZoom;
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
     * Is the player to be shown in Picture Mode.
     *
     * @return Returns boolean on if the player is to be shown.
     */
    public boolean isPlayerShown() {
        return showPlayer;
    }

    /**
     * Toggles on if the player is to be shown in Picture Mode.
     */
    public void togglePlayerShown() {
        showPlayer = !showPlayer;
    }

    /**
     * Ticks all the ticking callbacks for this state
     */
    public void tick() {
        Iterator<Tickable> iterator = tickingCallbacks.iterator();
        while (iterator.hasNext()) {
            Tickable tickable = iterator.next();
            boolean contTick = tickable.onTick();

            if (!contTick) {
                iterator.remove();
            }
        }
    }

    /**
     * Resets the Picture Mode state back to defaults
     */
    public void resetState() {
        cameraRotation.setValue(PictureModeConstants.DEFAULT_ROTATION);
        cameraTilt.setValue(PictureModeConstants.DEFAULT_TILT);
        cameraZoom.setValue(1.0D);
        fog.setValue(1.0D);
        cameraPanX.setValue(0.0D);
        cameraPanY.setValue(0.0D);
//        shaderIntensity.setValue(1.0D);

        showPlayer = true;
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

        //HACK: Reload all chunks if using VulkanMod.
        // This is because when Backface Culling is enabled,
        // some chunks do not render at all until they're refreshed.
        if (ModStatus.HAS_VULKANMOD) {
            Minecraft.getInstance().levelRenderer.allChanged();
        }
    }

    /**
     * Adds a callback for the tickable value to the callback list
     *
     * @param tickable The tickable value to add the callback for
     */
    public void addTickableCallback(Tickable tickable) {
        tickingCallbacks.add(tickable);
    }

    /**
     * Adds callbacks for tickable values to the callback list
     *
     * @param clazz    The class to add tickable callbacks from
     * @param instance The instance of the class to add callbacks from
     */
    public void addTickableCallbacks(Class<?> clazz, Object instance) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (!field.getType().isAssignableFrom(Value.class))
                    continue;

                boolean accessible = field.canAccess(instance);
                if (!accessible) {
                    field.setAccessible(true);
                }

                Object value = field.get(instance);
                if (value instanceof Tickable tickable)
                    addTickableCallback(tickable);

                if (!accessible) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Failed to add ticking callbacks for class {}!", clazz.getName(), e);
        }
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
