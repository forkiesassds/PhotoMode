package mod.icanttellyou.picturemode.forgelike.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.keymap.PictureModeKeymaps;
//? if >=1.21.6
import mod.icanttellyou.picturemode.client.render.shader.ShaderPatchHandler;
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
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
//? } else {
/*import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///? }

//? if neoforge
@Mod(value = PictureMode.MOD_ID, dist = Dist.CLIENT)
public class PictureModeForgelikeClient {
    @SuppressWarnings("InstantiationOfUtilityClass")
    public PictureModeForgelikeClient(IEventBus eventBus) {
        //? if neoforge {
        IEventBus gameBus = NeoForge.EVENT_BUS;
        //? } else {
        //IEventBus gameBus = MinecraftForge.EVENT_BUS;
        //? }

        gameBus.register(new GameRenderEventListeners());
        gameBus.register(new GameTickEventListeners());
        gameBus.register(new LevelEventListeners());

        eventBus.addListener(PictureModeForgelikeClient::onInitialize);
        eventBus.addListener(PictureModeForgelikeClient::registerBindings);
        //? if >=1.21.6
        eventBus.addListener(PictureModeForgelikeClient::addClientReloadListeners);
    }

    public static void onInitialize(FMLClientSetupEvent event) {
        PictureModeClient.commonInit();

        if (PictureModeServices.PLATFORM.isModPresent("yet_another_config_lib_v3")) {
            ModLoadingContext.get().registerExtensionPoint(
                //? if neoforge {
                IConfigScreenFactory.class,
                () ->
                //? } else {
                /*ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory
                *///? }
                ((client, parent) -> ConfigHelper.getConfigScreen(parent, PictureModeClient.getConfig()))
            );
        }
    }

    public static void registerBindings(RegisterKeyMappingsEvent event) {
        PictureModeKeymaps.initialise(event::register);
    }

    //? if >=1.21.6 {
    public static void addClientReloadListeners(net.neoforged.neoforge.client.event.AddClientReloadListenersEvent event) {
        PictureModeClient.initialiseShaderPatchHandler();
        event.addListener(ShaderPatchHandler.RELOAD_LISTENER_ID, PictureModeClient.getShaderPatchHandler());
        event.addDependency(net.neoforged.neoforge.client.resources.VanillaClientListeners.SHADERS,
                ShaderPatchHandler.RELOAD_LISTENER_ID);
    }
    //? }
}
