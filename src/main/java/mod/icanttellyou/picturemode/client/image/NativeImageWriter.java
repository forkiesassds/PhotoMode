package mod.icanttellyou.picturemode.client.image;

import com.mojang.blaze3d.platform.NativeImage;
import mod.icanttellyou.picturemode.client.image.format.NativeImageFormat;

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Set;

/**
 * A custom {@link NativeImage} writer, that can export to formats other than PNG
 */
public class NativeImageWriter {
    private static final Set<StandardOpenOption> OPEN_OPTIONS =
            EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    /**
     * Writes an image to file on disk.
     *
     * @param image  The image to write.
     * @param format The file format to write as.
     * @param file   The destination for the file.
     * @throws IOException The exception for when writing the image fails.
     */
    public static void writeToFile(NativeImage image, NativeImageFormat format, File file) throws IOException {
        writeToFile(image, format, file.toPath());
    }

    /**
     * Writes an image to file on disk.
     *
     * @param image  The image to write.
     * @param format The file format to write as.
     * @param path   The path to the file
     * @throws IOException The exception for when writing the image fails.
     */
    public static void writeToFile(NativeImage image, NativeImageFormat format, Path path) throws IOException {
        if (!format.imageValidForFormat(image.format())) {
            throw new UnsupportedOperationException("Don't know how to write pixel format " + image.format()
                    + " for file format " + format.getFormatName());
        }

        checkImageAllocated(image);
        try (WritableByteChannel channel = Files.newByteChannel(path, OPEN_OPTIONS)) {
            format.write(image, channel);
        } catch (Exception e) {
            throw new IOException("Could not write image to file \"" + path.toAbsolutePath() + "\"", e);
        }
    }

    private static void checkImageAllocated(NativeImage image) {
        //? if >=1.21.5 {
        long pointer = image.getPointer();
        //? } else {
        /*long pointer = ((mod.icanttellyou.picturemode.mixin.NativeImageAccessor) (Object) image).getPixels();
         *///? }

        if (pointer == 0L)
            throw new IllegalStateException("Image is not allocated.");
    }
}
