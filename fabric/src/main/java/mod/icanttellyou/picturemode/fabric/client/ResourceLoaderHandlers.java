//? if >=1.21.6 {
package mod.icanttellyou.picturemode.fabric.client;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.render.shader.ShaderPatchHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.server.packs.PackType;

public class ResourceLoaderHandlers {
    public static void initialise() {
        PictureModeClient.initialiseShaderPatchHandler();

        ResourceLoader resourceLoader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        resourceLoader.registerReloader(ShaderPatchHandler.RELOAD_LISTENER_ID, PictureModeClient.getShaderPatchHandler());
        resourceLoader.addReloaderOrdering(ResourceReloaderKeys.Client.SHADERS, ShaderPatchHandler.RELOAD_LISTENER_ID);
    }
}
//? }