package mod.icanttellyou.picturemode.client.image;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.stb.STBIWriteCallback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class ImageWriteCallback extends STBIWriteCallback {
    private final WritableByteChannel output;
    private @Nullable IOException exception;

    public ImageWriteCallback(WritableByteChannel output) {
        this.output = output;
    }

    @Override
    public void invoke(final long context, final long data, final int size) {
        ByteBuffer dataBuf = getData(data, size);

        try {
            this.output.write(dataBuf);
        } catch (IOException e) {
            this.exception = e;
        }
    }

    public void throwIfException() throws IOException {
        if (this.exception != null) {
            throw this.exception;
        }
    }
}
