package mod.icanttellyou.picturemode.forgelike.events;

import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
*///? }

//? if neoforge {
@EventBusSubscriber(
//?} else {
/*@Mod.EventBusSubscriber(
*///?}
    modid = PictureMode.MOD_ID,
    //? if neoforge && <1.21.1 {
    /*bus = EventBusSubscriber.Bus.MOD,
    *///?} else if forge {
    /*bus = Mod.EventBusSubscriber.Bus.MOD,
    *///?}
    value = Dist.CLIENT
)
public class ModEventsClient {
    @SubscribeEvent
    public static void onInitialize(FMLClientSetupEvent event) {
        /*if (FMLLoader.getLoadingModList().getModFileById("yet_another_config_lib_v3") != null) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) ->
                            ConfigHelper.getConfigScreen(parent, FMLPaths.CONFIGDIR.get(), PictureModeClient.config)
            );
        }*/

        PictureModeClient.config = PictureModeClientConfig.readConfig(FMLPaths.CONFIGDIR.get());
    }
}
