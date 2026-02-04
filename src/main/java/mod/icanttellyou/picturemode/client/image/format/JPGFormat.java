package mod.icanttellyou.picturemode.client.image.format;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.image.ImageWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class JPGFormat implements NativeImageFormat {
    /**
     * Gets the name of the format
     *
     * @return The format name
     */
    @Override
    public String getFormatName() {
        return "jpg";
    }

    /**
     * Writes the image to a buffer
     *
     * @param image   The image to write
     * @param channel The buffer to write into.
     */
    @Override
    public void write(NativeImage image, WritableByteChannel channel) throws IOException {
        try (ImageWriteCallback writeCallback = new ImageWriteCallback(channel)) {
            int write = STBImageWrite.nstbi_write_jpg_to_func(
                writeCallback.address(),
                0L,
                image.getWidth(),
                this.getProperHeight(image),
                image.format().components(),
                //? if >=1.21.5 {
                image.getPointer(),
                //? } else {
                /*((mod.icanttellyou.picturemode.mixin.NativeImageAccessor) (Object) image).getPixels(),
                *///? }
                NativeImageFormat.<Config>getConfig(this).quality
            );

            writeCallback.throwIfException();
            if (write == 0)
                throw new IOException("Failed to write image: " + STBImage.stbi_failure_reason());
        }
    }

    /**
     * Gets the codec for format's config provider
     *
     * @return The format's config provider codec
     */
    @SuppressWarnings("unchecked")
    @Override
    public Codec<Config> getConfigProviderCodec() {
        return Config.CODEC;
    }

    /**
     * Provides the config provider for the format
     *
     * @return A new instance of the config provider for the format
     */
    @Override
    public ConfigProvider provideConfigProvider() {
        return new Config();
    }

    public static class Config implements ConfigProvider {
        public static final Codec<Config> CODEC = Codec.intRange(0, 100).optionalFieldOf("quality", 75).codec()
                .xmap(Config::new, config -> config.quality);

        public int quality;

        public Config() {
            this(75);
        }

        public Config(int quality) {
            this.quality = quality;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Codec<Config> getCodec() {
            return CODEC;
        }

        /**
         * Provides the config GUI options for the format
         *
         * @param builder      The config GUI builder instance
         * @param mainCategory The main category builder instance
         */
        @Override
        public void provideConfigOptions(
            YetAnotherConfigLib.Builder builder,
            ConfigCategory.Builder mainCategory
        ) {
            mainCategory.group(OptionGroup.createBuilder()
                .name(ConfigHelper.getConfigText("format.jpg.settings"))
                .option(Option.<Integer>createBuilder()
                    .name(ConfigHelper.getConfigText("format.jpg.quality.name"))
                    .description(OptionDescription.of(ConfigHelper.getConfigText("format.jpg.quality.desc")))
                    .binding(75,
                        () -> this.quality, newVal -> this.quality = newVal)
                    .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(0, 100)
                        .step(1))
                    .build())
                .build());
        }
    }
}
