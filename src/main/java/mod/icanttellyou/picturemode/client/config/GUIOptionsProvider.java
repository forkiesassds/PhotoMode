package mod.icanttellyou.picturemode.client.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;

/**
 * A class for providing GUI options
 *
 * @apiNote This is a class, as to avoid loading YACL at runtime.
 */
public abstract class GUIOptionsProvider {
    /**
     * Provides the config GUI options for the format
     *
     * @param builder      The config GUI builder instance
     * @param mainCategory The main category builder instance
     */
    public abstract void provide(
        YetAnotherConfigLib.Builder builder,
        ConfigCategory.Builder mainCategory
    );
}
