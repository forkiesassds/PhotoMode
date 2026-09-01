//? if >=26.3 {
/*package mod.icanttellyou.picturemode.client.image;

import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;

import java.lang.invoke.MethodHandles;

import static org.lwjgl.system.APIUtil.apiClosureRet;
import static org.lwjgl.system.APIUtil.apiCreateCIF;
import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.MemoryUtil.memGetLong;
import static org.lwjgl.system.libffi.LibFFI.*;

@FunctionalInterface
public interface SPNGWriteCallbackI extends CallbackI {
    Callback.Descriptor DESCRIPTOR = new Callback.Descriptor(
        SPNGWriteCallbackI.class,
        MethodHandles.lookup(),
        apiCreateCIF(
            ffi_type_sint32,
            ffi_type_pointer, ffi_type_pointer, ffi_type_pointer, ffi_type_sint64
        )
    );

    @Override
    default Callback.Descriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    default void callback(long ret, long args) {
        int __result = invoke(
            memGetAddress(memGetAddress(args)),
            memGetAddress(memGetAddress(args + POINTER_SIZE)),
            memGetAddress(memGetAddress(args + 2 * POINTER_SIZE)),
            memGetLong(memGetAddress(args + 3 * POINTER_SIZE))
        );
        apiClosureRet(ret, __result);
    }

    int invoke(final long ctx, final long user, final long dest, final long length);
}
*///? }