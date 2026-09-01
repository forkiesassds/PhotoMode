package mod.icanttellyou.picturemode.client.image.format;

import com.mojang.blaze3d.platform.NativeImage;
import mod.icanttellyou.picturemode.client.image.*;
//? if >=26.3 {
/*import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.spng.SPNG;
import org.lwjgl.util.spng.spng_ihdr;
*///? } else {
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
//? }

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class PNGFormat implements NativeImageFormat {
    /**
     * Gets the name of the format
     *
     * @return The format name
     */
    @Override
    public String getFormatName() {
        return "png";
    }

    /**
     * Writes the image to a buffer
     *
     * @param image   The image to write
     * @param channel The buffer to write into.
     */
    @Override
    public void write(NativeImage image, WritableByteChannel channel) throws IOException {
        //? if >=26.3 {
        /*long context = SPNG.spng_ctx_new(SPNG.SPNG_CTX_ENCODER);

        try (MemoryStack stack = MemoryStack.stackPush();
             SPNGWriteCallback writeCallback = new SPNGWriteCallback(channel);) {
            checkSpngError("set output", SPNG.nspng_set_png_stream(context, writeCallback.address(), 0L));
            spng_ihdr header = spng_ihdr.calloc(stack)
                .width(image.getWidth())
                .height(this.getProperHeight(image))
                .color_type((byte)((mod.icanttellyou.picturemode.mixin.NativeImageFormatAccessor) (Object) image.format()).getPngColorType())
                .bit_depth((byte)8);
            checkSpngError("set header", SPNG.spng_set_ihdr(context, header));
            checkSpngError("write image", SPNG.nspng_encode_image(
                context,
                image.getPointer(),
                ((mod.icanttellyou.picturemode.mixin.NativeImageAccessor) (Object) image).getSize(),
                SPNG.SPNG_FMT_PNG,
                SPNG.SPNG_ENCODE_FINALIZE
            ));
            writeCallback.throwIfException();
        } finally {
            SPNG.spng_ctx_free(context);
        }
        *///? } else {
        try (ImageWriteCallback writeCallback = new ImageWriteCallback(channel)) {
            int write = STBImageWrite.nstbi_write_png_to_func(
                writeCallback.address(),
                0L,
                image.getWidth(),
                this.getProperHeight(image),
                image.format().components(),
                //? if >=1.21.5 {
                image.getPointer(),
                //? } else {
                //((mod.icanttellyou.picturemode.mixin.NativeImageAccessor) (Object) image).getPixels(),
                //? }
                0
            );

            writeCallback.throwIfException();
            if (write == 0)
                throw new IOException("Failed to write image: " + STBImage.stbi_failure_reason());
        }
        //? }
    }

    //? if >=26.3 {
    /*private static void checkSpngError(final String operation, final int result) throws IOException {
        if (result != 0) {
            throw new IOException("SPNG operation '" + operation + "' failed: " + SPNG.spng_strerror(result) + " (" + result + ")");
        }
    }
    *///? }
}
