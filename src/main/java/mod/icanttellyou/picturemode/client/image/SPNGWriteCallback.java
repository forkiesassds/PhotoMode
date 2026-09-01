//? if >=26.3 {
/*package mod.icanttellyou.picturemode.client.image;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.Callback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import static org.lwjgl.system.MemoryUtil.*;

public class SPNGWriteCallback extends Callback implements SPNGWriteCallbackI {
    private final WritableByteChannel output;
    private @Nullable IOException exception;

    public SPNGWriteCallback(WritableByteChannel channel) {
        super(SPNGWriteCallbackI.DESCRIPTOR);
        this.output = channel;
    }

    @Override
    public int invoke(final long ctx, final long user, final long dest, final long length) {
        ByteBuffer dataBuf = memByteBuffer(dest, (int) length);

        try {
            this.output.write(dataBuf);
            return 0;
        } catch (IOException e) {
            this.exception = e;
            return -2;
        }
    }

    public void throwIfException() throws IOException {
        if (this.exception != null) {
            throw this.exception;
        }
    }
}
*///? }