package mod.icanttellyou.picturemode.forgelike;

import mod.icanttellyou.picturemode.services.IPlatformHelper;
//? if neoforge {
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
//?} else {
/*import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
*///?}

import java.nio.file.Path;

public class FMLPlatformHelper implements IPlatformHelper {
    public boolean isModPresent(String mod) {
        //? if <1.21 {
        /*if (mod.equals("sodium")) {
            return isModPresent("embeddium") || isModPresent("rubidium");
        }
        *///? }

        return FMLLoader/*? >=1.21.9 {*/.getCurrent()/*?}*/.getLoadingModList().getModFileById(mod) != null;
    }

    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader/*? >=1.21.9 {*/.getCurrent()/*?}*/.isProduction();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
