package mod.icanttellyou.picturemode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder;
import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormat;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormats;
import mod.icanttellyou.picturemode.services.PictureModeServices;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.event.Level;

import java.lang.reflect.Field;
import java.nio.file.Path;

public class ConfigHelper {
    public static Screen getConfigScreen(Screen parent, PictureModeClientConfig config) {
        Path configPath = PictureModeServices.PLATFORM.getConfigDir();
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
            .name(getConfigText("title"))
            .group(OptionGroup.createBuilder()
                .option(Option.<Boolean>createBuilder()
                    .name(getConfigText("buttonInPauseMenu.name"))
                    .description(OptionDescription.of(getConfigText("buttonInPauseMenu.desc")))
                    .binding(true, () -> config.buttonInPauseMenu, newVal -> config.buttonInPauseMenu = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(getConfigText("preserveSettings.name"))
                    .description(OptionDescription.of(getConfigText("preserveSettings.desc")))
                    .binding(true, () -> config.preserveSettings, newVal -> config.preserveSettings = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())
                .option(Option.<NativeImageFormat>createBuilder()
                    .name(getConfigText("format.name"))
                    .description(OptionDescription.of(getConfigText("format.desc")))
                    .binding(NativeImageFormats.PNG_FORMAT,
                        () -> config.format, newVal -> config.format = newVal)
                    .controller(opt ->
                        CyclingListControllerBuilder.create(opt).values(NativeImageFormats.FORMATS)
                            .formatValue(val -> getConfigText("format." + val.getFormatName())))
                    .build())
                .build()
            );

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
            .title(getConfigText("title"));

        try {
            for (Field f : config.getClass().getFields()) {
                if (!AbstractGUIOptionsProviderFactory.class.isAssignableFrom(f.getType()))
                    continue;

                AbstractGUIOptionsProviderFactory factory = (AbstractGUIOptionsProviderFactory) f.get(config);
                factory.getGUIOptionsProvider().provide(builder, categoryBuilder);
            }
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Failed to populate some GUI entries!", e);
        }

        for (NativeImageFormat.ConfigProvider configProvider : config.formatSettings.values()) {
            if (configProvider == null)
                continue;

            configProvider.getGUIOptionsProvider().provide(builder, categoryBuilder);
        }

        return builder
            .category(categoryBuilder.build())
            .save(() -> config.saveConfig(configPath))
            .build().generateScreen(parent);
    }

    public static Component getConfigText(String text) {
        return Component.translatable(PictureMode.MOD_ID + ".config." + text);
    }

    public static Component getConfigText(String text, Object... keys) {
        return Component.translatable(PictureMode.MOD_ID + ".config." + text, keys);
    }
}
