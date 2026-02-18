package mod.icanttellyou.picturemode.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    boolean isModPresent(String mod);
    boolean isDevEnvironment();

    Path getConfigDir();
}
