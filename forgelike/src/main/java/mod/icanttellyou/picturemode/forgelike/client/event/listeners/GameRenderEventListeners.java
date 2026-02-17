package mod.icanttellyou.picturemode.forgelike.client.event.listeners;

//? if >=1.21.6
import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
//? if >=1.21.6
import net.neoforged.neoforge.client.event.ViewportEvent;
//? } else {
//? }

public final class GameRenderEventListeners {
    //? if neoforge && >=1.21.6 {
    @SubscribeEvent
    public void onFogSetup(ViewportEvent.RenderFog event) {
        if (event.getEnvironment() instanceof PMModifiableFog pmModifiableFog) {
            pmModifiableFog.pictureMode$modifyFog(event.getFogData(), event.getPartialTick());
        }
    }
    //? }
}
