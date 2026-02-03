package mod.icanttellyou.picturemode.client.image.format;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * An interface for a format for writing {@link NativeImage} objects
 */
public interface NativeImageFormat {
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

    interface ConfigProvider {
        <P extends ConfigProvider> Codec<P> getCodec();
    }
}
