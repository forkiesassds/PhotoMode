package mod.icanttellyou.picturemode.client;

public class PictureModeClient {
    private static PictureModeState state;

    /**
     * Gets the current Picture Mode state
     *
     * @return The current Picture Mode state
     */
    public static PictureModeState getState() {
        if (state == null)
            throw new IllegalStateException("Picture Mode state is null!");

        return state;
    }

    /**
     * Gets the current Picture Mode state. Does not check if the state is null.
     *
     * @return The current Picture Mode state, or null if not initialised
     */
    public static PictureModeState getStateUnsafe() {
        return state;
    }

    public static void onWorldLoad() {
        if (state != null)
            return;

        state = new PictureModeState();
    }

    public static void onWorldExit() {
        state = null;
    }
}
