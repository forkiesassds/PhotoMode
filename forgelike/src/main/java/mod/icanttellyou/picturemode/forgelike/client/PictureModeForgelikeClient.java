package mod.icanttellyou.picturemode.forgelike.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.config.PictureModeClientConfig;
import mod.icanttellyou.picturemode.forgelike.client.event.listeners.GameRenderEventListeners;
import mod.icanttellyou.picturemode.forgelike.client.event.listeners.GameTickEventListeners;
import mod.icanttellyou.picturemode.forgelike.client.event.listeners.LevelEventListeners;
import mod.icanttellyou.picturemode.services.PictureModeServices;
//? if neoforge {
import mod.icanttellyou.picturemode.PictureMode;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
//? } else {
/*import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
*///? }

//? if neoforge
@Mod(value = PictureMode.MOD_ID, dist = Dist.CLIENT)
public class PictureModeForgelikeClient {
    @SuppressWarnings("InstantiationOfUtilityClass")
    public PictureModeForgelikeClient(IEventBus eventBus) {
        //? if neoforge {
        IEventBus gameBus = NeoForge.EVENT_BUS;
        //? } else {
        /*IEventBus gameBus = MinecraftForge.EVENT_BUS;
        *///? }

        gameBus.register(new GameRenderEventListeners());
        gameBus.register(new GameTickEventListeners());
        gameBus.register(new LevelEventListeners());

        eventBus.addListener(PictureModeForgelikeClient::onInitialize);
    }

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
