package mod.icanttellyou.picturemode.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("yet_another_config_lib_v3"))
            return parent -> ConfigHelper.getConfigScreen(parent, loader.getConfigDir(), PictureModeClient.config);

        return null;
    }
}
