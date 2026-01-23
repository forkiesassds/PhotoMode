package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.util.LevelUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    @SuppressWarnings("DataFlowIssue")
    protected ClientLevelMixin() {
        super(
            null,
            null,
            null,
            null,
            //? <1.21.2
            //null,
            false,
            false,
            0,
            0
        );
    }

    @Override
    //? if >=1.21.11 {
    public long getDayTime() {
        long dayTime = super.getDayTime();
    //? } else {
    /*public long dayTime() {
        long dayTime = super.dayTime();
    *///? }
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return dayTime;

        double override = state.timeOverride.getValue(0.0D);
        if (override == 0.0D)
            return dayTime;

        int dayLength = LevelUtils.getDayLength(this);

        long dayBase = dayTime / dayLength * dayLength;
        return (long) (dayBase + override);
    }
}
