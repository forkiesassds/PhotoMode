package mod.icanttellyou.picturemode.forgelike.events;

import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.PictureModeClient;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
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
    public static void onLevelUnload(LevelEvent.Unload event) {
        PictureModeClient.onWorldExit();
    }
}
