//? if >=1.21.6 {
package mod.icanttellyou.picturemode.imixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.fog.FogData;

/**
 * A mixin helper for modifying fog based on the Picture Mode state
 */
public interface PMModifiableFog {
    /**
     * Modifies the {@link net.minecraft.client.renderer.fog.environment.FogEnvironment}
     * values for the current Picture Mode state.
     *
     * @param data         The {@link FogData} instance to modify
     * @param deltaTracker The {@link DeltaTracker} for getting current fog modifier
     */
    default void pictureMode$modifyFog(FogData data, DeltaTracker deltaTracker) {
        PictureModeState state = PictureModeClient.getState();

        if (!state.isEnabled())
            return;

        float fogModifier = (float) state.fog.getValue(deltaTracker.getGameTimeDeltaPartialTick(true));
        data.environmentalStart *= fogModifier;
        data.environmentalEnd *= fogModifier;
    }
}
//? }