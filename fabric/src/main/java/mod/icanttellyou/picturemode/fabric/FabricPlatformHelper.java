package mod.icanttellyou.picturemode.fabric;

import mod.icanttellyou.picturemode.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
    private final FabricLoader loader = FabricLoader.getInstance();

    @Override
    public boolean isModPresent(String mod) {
        return loader.isModLoaded(mod);
    }

    @Override
    public boolean isDevEnvironment() {
        return loader.isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigDir() {
        return loader.getConfigDir();
    }
}
