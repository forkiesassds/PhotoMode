package mod.icanttellyou.picturemode.forgelike.event.listeners;

import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
//? if >=1.21.6
import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
import net.minecraft.client.Minecraft;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
//? if <1.20.5
//import net.neoforged.neoforge.event.TickEvent
//? if >=1.21.6
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
*///? }

//? if neoforge {
@EventBusSubscriber(
//?} else {
/*@Mod.EventBusSubscriber(
*///?}
    modid = PictureMode.MOD_ID,
    //? if neoforge && <1.21.1 {
    /*bus = EventBusSubscriber.Bus.GAME,
    *///?} else if forge {
    /*bus = Mod.EventBusSubscriber.Bus.FORGE,
    *///?}
    value = Dist.CLIENT
)
public class GameEventsClient {
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        PictureModeClient.onWorldLoad();
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        PictureModeClient.onWorldExit();
    }

    @SubscribeEvent
    public static void onGameTick(
        //? if neoforge && >=1.20.5 {
        ClientTickEvent.Pre
        //? } else {
        /*TickEvent.ClientTickEvent
        *///? }
        event
    ) {
        //? if (forge && <1.21.1) || <1.20.5 {
        /*if (event.phase != TickEvent.Phase.END)
            return;
        *///? }

        Minecraft mc = Minecraft.getInstance();
        PictureModeState state = PictureModeClient.getState();

        if (mc.level == null || state == null)
            return;

        state.tick();
    }

    //? if neoforge && >=1.21.6 {
    @SubscribeEvent
    public static void onFogSetup(ViewportEvent.RenderFog event) {
        if (event.getEnvironment() instanceof PMModifiableFog pmModifiableFog) {
            pmModifiableFog.pictureMode$modifyFog(event.getFogData(), event.getPartialTick());
        }
    }
    //? }
}
