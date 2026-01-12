package mod.icanttellyou.photomode.services;

import mod.icanttellyou.photomode.util.LoggingUtil;
import org.slf4j.event.Level;

import java.util.ServiceLoader;

public class PhotoModeServices {
    public static IPlatformHelper PLATFORM = loadService(IPlatformHelper.class);


    private static <T> T loadService(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LoggingUtil.log(Level.DEBUG, "Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
