package mod.icanttellyou.picturemode.forgelike.event.listeners;

import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.services.PictureModeServices;
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//? } else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
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
        if (PictureModeServices.PLATFORM.isModPresent("yet_another_config_lib_v3")) {
            ModLoadingContext.get().registerExtensionPoint(
                //? if neoforge {
                IConfigScreenFactory.class,
                () ->
                //? } else {
                /*ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory
                *///? }
                ((client, parent) -> ConfigHelper.getConfigScreen(parent, FMLPaths.CONFIGDIR.get(), PictureModeClient.config))
            );
        }

        PictureModeClient.config = PictureModeClientConfig.readConfig(FMLPaths.CONFIGDIR.get());
    }
}
