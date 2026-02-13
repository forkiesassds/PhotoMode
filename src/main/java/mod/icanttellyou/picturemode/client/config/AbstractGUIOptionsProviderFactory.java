package mod.icanttellyou.picturemode.client.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;

/**
 * An abstract factory for {@link GUIOptionsProvider}
 */
public interface AbstractGUIOptionsProviderFactory {
    /**
     * Gets the config GUI options provider
     *
     * @return The config GUI options provider for the format
     */
    default GUIOptionsProvider getGUIOptionsProvider() {
        return new GUIOptionsProvider() {
            @Override
            public void provide(YetAnotherConfigLib.Builder builder, ConfigCategory.Builder mainCategory) {}
        };
    }
}
