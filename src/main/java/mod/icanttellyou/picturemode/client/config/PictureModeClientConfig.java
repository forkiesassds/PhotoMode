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
import java.util.Iterator;
import java.util.Map;

public class PictureModeClientConfig {
    @SuppressWarnings("unchecked")
    public static final Codec<PictureModeClientConfig> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("format").xmap(NativeImageFormats::getFormat, NativeImageFormats::getFormatId)
                        .forGetter(conf -> conf.format),
                Codec.<Map<String, NativeImageFormat.ConfigProvider>>of(new Encoder<>() {
                    @Override
                    public <T> DataResult<T> encode(Map<String, NativeImageFormat.ConfigProvider> o, DynamicOps<T> ops, T t) {
                        RecordBuilder<T> mapBuilder = ops.mapBuilder();

                        for (Map.Entry<String, NativeImageFormat.ConfigProvider> entry : o.entrySet()) {
                            String formatKey = entry.getKey();
                            NativeImageFormat.ConfigProvider formatConfig = entry.getValue();

                            DataResult<T> key = Codec.STRING.encodeStart(ops, formatKey);
                            DataResult<T> value = formatConfig.getCodec().encodeStart(ops, formatConfig);

                            if (value.error().isPresent()) {
                                //noinspection OptionalGetWithoutIsPresent
                                return DataResult.error(() -> "Failed to encode config for format " + formatKey + ": "
                                        + value.error().get());
                            }

                            mapBuilder.add(key, value);
                        }
                        return mapBuilder.build(ops.emptyMap());
                    }
                }, new Decoder<>() {
                    @Override
                    public <T> DataResult<Pair<Map<String, NativeImageFormat.ConfigProvider>, T>> decode(DynamicOps<T> ops, T t) {
                        DataResult<MapLike<T>> mapResult = ops.getMap(t);
                        MapLike<T> map = mapResult.result().orElseThrow();

                        ImmutableMap.Builder<String, NativeImageFormat.ConfigProvider> mapBuilder = ImmutableMap.builder();
                        Iterator<Pair<T, T>> iterator = map.entries().iterator();
                        while (iterator.hasNext()) {
                            Pair<T, T> entry = iterator.next();
                            String key = Codec.STRING.parse(ops, entry.getFirst()).result().orElseThrow();

                            NativeImageFormat fileFormat = NativeImageFormats.FORMATS.get(key);
                            if (fileFormat == null) {
                                return DataResult.error(() -> "No such format as " + key + " exists");
                            }

                            Codec<NativeImageFormat.ConfigProvider> codec = fileFormat.getConfigProviderCodec();
                            DataResult<NativeImageFormat.ConfigProvider> configResult = codec.parse(ops, entry.getSecond());

                            if (configResult.error().isPresent()) {
                                //noinspection OptionalGetWithoutIsPresent
                                return DataResult.error(() -> "Failed to parse format " + key + ": " +
                                        configResult.error().get().message());
                            }
                            NativeImageFormat.ConfigProvider config = configResult.result().orElseThrow();

                            mapBuilder.put(key, config);
                        }

                        return DataResult.success(Pair.of(mapBuilder.build(), ops.empty()));
                    }
                }).fieldOf("format_settings").forGetter(conf -> conf.formatSettings)
        ).apply(instance, PictureModeClientConfig::new));

    public NativeImageFormat format;
    public Map<String, NativeImageFormat.ConfigProvider> formatSettings;

    public PictureModeClientConfig() {
        this(NativeImageFormats.getFormat("png"), ImmutableMap.of());
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
