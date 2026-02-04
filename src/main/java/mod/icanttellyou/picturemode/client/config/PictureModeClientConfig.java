package mod.icanttellyou.picturemode.client.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormat;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormats;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PictureModeClientConfig {
    public static final Codec<PictureModeClientConfig> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("format").xmap(NativeImageFormats::getFormat, NativeImageFormat::getFormatName)
                        .forGetter(conf -> conf.format),
                NativeImageFormat.ConfigProvider.CONFIG_MAP_CODEC.fieldOf("format_settings")
                        .forGetter(conf -> conf.formatSettings)
        ).apply(instance, PictureModeClientConfig::new));

    public NativeImageFormat format;
    public Map<String, NativeImageFormat.ConfigProvider> formatSettings;

    public PictureModeClientConfig() {
        this(
            NativeImageFormats.PNG_FORMAT,
            NativeImageFormats.FORMATS.stream()
                .map(f -> Pair.of(f.getFormatName(), f.provideConfigProvider()))
                .filter(entry -> entry.getFirst() != null && entry.getSecond() != null)
                .collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond))
        );
    }

    public PictureModeClientConfig(NativeImageFormat format, Map<String, NativeImageFormat.ConfigProvider> formatSettings) {
        this.format = format;
        this.formatSettings = formatSettings;
    }

    private final static String CONFIG_PATH = PictureMode.MOD_ID + "/" + PictureMode.MOD_ID + "_client.json";

    public static PictureModeClientConfig readConfig(Path configDir) {
        Path configFile = configDir.resolve(CONFIG_PATH);
        try (BufferedReader reader = Files.newBufferedReader(configFile)) {
            DataResult<Pair<PictureModeClientConfig, JsonElement>> result =
                    CODEC.decode(JsonOps.INSTANCE, new Gson().fromJson(reader, JsonElement.class));

            if (result.error().isPresent()) {
                LoggingUtil.log(Level.ERROR, "Encountered error parsing config file: {}",
                        result.error().orElseThrow().message());
            }

            return result.result()
                    .orElseGet(() -> Pair.of(new PictureModeClientConfig(), null))
                    .getFirst();
        } catch (IOException e) {
            return new PictureModeClientConfig();
        }
    }

    public void saveConfig(Path configDir) {
        Path configFile = configDir.resolve(CONFIG_PATH);

        DataResult<JsonElement> encodedConfig = CODEC.encode(this, JsonOps.INSTANCE, new JsonObject());
        if (encodedConfig.result().isEmpty()) {
            LoggingUtil.log(Level.WARN, "Failed to serialise JSON: {}", encodedConfig);
            return;
        }

        try {
            Files.createDirectories(configDir);
            try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(encodedConfig.result().get(), writer);
            }
        } catch (IOException e) {
            LoggingUtil.log(Level.ERROR, "Failed to write config file: ", e);
        }
    }
}
