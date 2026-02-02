package mod.icanttellyou.picturemode.client.image.format;

import com.mojang.blaze3d.platform.NativeImage;
import mod.icanttellyou.picturemode.client.image.ImageWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class JPGFormat implements NativeImageFormat {
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
                CONFIG.quality.value()
            );

            writeCallback.throwIfException();
            if (write == 0)
                throw new IOException("Failed to write image: " + STBImage.stbi_failure_reason());
        }
    }
}
