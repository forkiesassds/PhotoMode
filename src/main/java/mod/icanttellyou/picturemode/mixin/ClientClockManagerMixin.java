//? if >=26.1 {
/*package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.util.LevelUtils;
import net.minecraft.client.ClientClockManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//~ if >=26.3 'ClientClockManager.class' -> 'ClientClockManager.ClientClockInstance.class'
@Mixin(ClientClockManager.class)
public abstract class ClientClockManagerMixin {
    //~ if >=26.3 'getTotalTicks' -> 'totalTicks'
    @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
    private void overrideTotalTicks(CallbackInfoReturnable<Long> cir) {
        long dayTime = cir.getReturnValueJ();
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            return;

        double override = state.timeOverride.getValue(0.0D);
        if (override == 0.0D)
            return;

        Minecraft mc = Minecraft.getInstance();
        //noinspection DataFlowIssue
        int dayLength = LevelUtils.getDayLength(mc.level);

        long dayBase = dayTime / dayLength * dayLength;
        cir.setReturnValue((long) (dayBase + override));
    }
}
*///? }