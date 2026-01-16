package mod.icanttellyou.picturemode.forgelike.events;

import mod.icanttellyou.picturemode.PictureMode;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
*///? }

//? if neoforge {
@EventBusSubscriber(
//?} else {
/*@Mod(PictureMode.MOD_ID)
@Mod.EventBusSubscriber(
*///?}
    modid = PictureMode.MOD_ID,
    //? if neoforge && <1.21.1 {
    /*bus = EventBusSubscriber.Bus.MOD,
    *///?} else if forge {
    /*bus = Mod.EventBusSubscriber.Bus.MOD,
    *///?}
    value = Dist.CLIENT
)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {}
}
