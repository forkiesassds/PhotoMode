package mod.icanttellyou.picturemode.client.image.format;

import com.mojang.blaze3d.platform.NativeImage;
import mod.icanttellyou.picturemode.client.image.ImageWriteCallback;
import mod.icanttellyou.picturemode.util.LoggingUtil;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class TGAFormat implements NativeImageFormat {
    /**
     * Writes the image to a buffer
     *
     * @param image   The image to write
     * @param channel The buffer to write into.
     */
    @Override
    public void write(NativeImage image, WritableByteChannel channel) throws IOException {
        try (ImageWriteCallback writeCallback = new ImageWriteCallback(channel)) {
            int write = STBImageWrite.nstbi_write_tga_to_func(
                writeCallback.address(),
                0L,
                image.getWidth(),
                this.getProperHeight(image),
                image.format().components(),
                //? if >=1.21.5 {
                image.getPointer()
                //? } else {
                /*((mod.icanttellyou.picturemode.mixin.NativeImageAccessor) (Object) image).getPixels()
                *///? }
            );

            writeCallback.throwIfException();
            if (write == 0)
                throw new IOException("Failed to write image: " + STBImage.stbi_failure_reason());
        }
    }

    /**
     * Gets proper height for image, based on format limitations.
     *
     * @param image The image to get proper height for
     * @return The proper height of the image.
     */
    @Override
    public int getProperHeight(NativeImage image) {
        int height = Math.min(image.getHeight(), 0xFFFF);
        if (height < image.getHeight()) {
            LoggingUtil.log(Level.WARN, "Dropping image height from {} to {} to fit the size into 16-bit unsigned int", image.getHeight(), height);
        }

        return height;
    }
}
