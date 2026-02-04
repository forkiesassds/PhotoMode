package mod.icanttellyou.picturemode.client.image.format;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An interface for a format for writing {@link NativeImage} objects
 */
public interface NativeImageFormat {
    /**
     * Gets the name of the format
     *
     * @return The format name
     */
    String getFormatName();

    /**
     * Checks if the image is valid for the given components
     *
     * @param components The image components to validate against
     * @return Whether the image is valid for the format
     */
    default boolean imageValidForFormat(NativeImage.Format components) {
        return components.supportedByStb();
    }

    /**
     * Writes the image to a buffer
     *
     * @param image   The image to write
     * @param channel The buffer to write into.
     */
    void write(NativeImage image, WritableByteChannel channel) throws IOException;

    /**
     * Gets proper height for image, based on format limitations.
     *
     * @param image The image to get proper height for
     * @return The proper height of the image.
     */
    default int getProperHeight(NativeImage image) {
        int height = Math.min(image.getHeight(), Integer.MAX_VALUE / image.getWidth() / image.format().components());
        if (height < image.getHeight()) {
            LoggingUtil.log(Level.WARN, "Dropping image height from {} to {} to fit the size into 32-bit signed int", image.getHeight(), height);
        }

        return height;
    }

    /**
     * Gets the codec for format's config provider
     *
     * @return The format's config provider codec
     */
    default <P extends ConfigProvider> Codec<P> getConfigProviderCodec() {
        return Codec.EMPTY.codec().xmap(a -> null, a -> null);
    }

    /**
     * Provides the config provider for the format
     *
     * @return A new instance of the config provider for the format
     */
    default ConfigProvider provideConfigProvider() {
        return null;
    }

    /**
     * Gets the config for the format. This does not initialise anything.
     *
     * @param format The format to get the config for
     * @return The config for the format
     * @param <P> The config type, used by the format
     */
    @SuppressWarnings("unchecked")
    static <P extends ConfigProvider> P getConfig(NativeImageFormat format) {
        //TODO: maybe do this better?
        return (P) PictureModeClient.config.formatSettings.get(format.getFormatName());
    }

    interface ConfigProvider {
        Codec<Map<String, ConfigProvider>> CONFIG_MAP_CODEC = Codec.of(new Encoder<>() {
            @Override
            public <T> DataResult<T> encode(Map<String, ConfigProvider> o, DynamicOps<T> ops, T t) {
                RecordBuilder<T> mapBuilder = ops.mapBuilder();

                for (Map.Entry<String, ConfigProvider> entry : o.entrySet()) {
                    String formatKey = entry.getKey();
                    ConfigProvider formatConfig = entry.getValue();

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
            public <T> DataResult<Pair<Map<String, ConfigProvider>, T>> decode(DynamicOps<T> ops, T t) {
                DataResult<MapLike<T>> mapResult = ops.getMap(t);
                MapLike<T> map = mapResult.result().orElseThrow();

                ImmutableMap.Builder<String, ConfigProvider> mapBuilder = ImmutableMap.builder();
                Set<String> found = new HashSet<>();

                Iterator<Pair<T, T>> iterator = map.entries().iterator();
                while (iterator.hasNext()) {
                    Pair<T, T> entry = iterator.next();
                    String key = Codec.STRING.parse(ops, entry.getFirst()).result().orElseThrow();

                    NativeImageFormat fileFormat = NativeImageFormats.FORMATS.stream()
                            .filter(format -> format.getFormatName().equals(key))
                            .findFirst()
                            .orElse(null);
                    if (fileFormat == null) {
                        return DataResult.error(() -> "No such format as " + key + " exists");
                    }

                    Codec<ConfigProvider> codec = fileFormat.getConfigProviderCodec();
                    DataResult<ConfigProvider> configResult = codec.parse(ops, entry.getSecond());

                    if (configResult.error().isPresent()) {
                        //noinspection OptionalGetWithoutIsPresent
                        return DataResult.error(() -> "Failed to parse format " + key + ": " +
                                configResult.error().get().message());
                    }
                    ConfigProvider config = configResult.result().orElseThrow();

                    mapBuilder.put(key, config);
                    found.add(key);
                }

                //Populate any missing formats not in the config
                for (NativeImageFormat format : NativeImageFormats.FORMATS) {
                    String key = format.getFormatName();
                    if (found.contains(key))
                        continue;

                    ConfigProvider configProvider = format.provideConfigProvider();
                    if (configProvider == null)
                        continue;

                    mapBuilder.put(key, configProvider);
                }

                return DataResult.success(Pair.of(mapBuilder.build(), ops.empty()));
            }
        });

        <P extends ConfigProvider> Codec<P> getCodec();

        /**
         * Provides the config GUI options for the format
         *
         * @param builder      The config GUI builder instance
         * @param mainCategory The main category builder instance
         */
        default void provideConfigOptions(
            YetAnotherConfigLib.Builder builder,
            ConfigCategory.Builder mainCategory
        ) {}
    }
}
