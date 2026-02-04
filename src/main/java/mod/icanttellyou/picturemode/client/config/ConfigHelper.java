package mod.icanttellyou.picturemode.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder;
import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormat;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormats;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class ConfigHelper {
    public static Screen getConfigScreen(Screen parent, Path configPath, PictureModeClientConfig config) {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
            .name(getConfigText("title"))
            .group(OptionGroup.createBuilder()
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

        for (NativeImageFormat.ConfigProvider configProvider : config.formatSettings.values()) {
            if (configProvider == null)
                continue;

            configProvider.provideConfigOptions(builder, categoryBuilder);
        }

        return builder
            .category(categoryBuilder.build())
            .save(() -> config.saveConfig(configPath))
            .build().generateScreen(parent);
    }

    public static Component getConfigText(String text) {
        return Component.translatable(PictureMode.MOD_ID + ".config." + text);
    }
}
