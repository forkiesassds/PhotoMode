package mod.icanttellyou.picturemode.forgelike.events;

import mod.icanttellyou.picturemode.PictureMode;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
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
}
