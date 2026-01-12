package mod.icanttellyou.picturemode.util;

/**
 * An interface for things that has to run each game tick
 */
@FunctionalInterface
public interface Tickable {
    /**
     * The callback to run on game tick
     */
    void onTick();
}
