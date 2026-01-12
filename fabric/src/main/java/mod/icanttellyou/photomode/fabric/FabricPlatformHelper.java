package mod.icanttellyou.photomode.fabric;

import mod.icanttellyou.photomode.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

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
}
