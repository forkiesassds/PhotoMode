package mod.icanttellyou.picturemode.fabric.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.GameRenderer;

/**
 * Mods should use these events to perform actions related to game rendering,
 * without adding injections into the game tick methods.
 * <p>
 * There are events for before and after the game has rendered into the main Render Target
 */
public final class OnGameRenderEvents {
    /**
     * An event triggered when the game is about to be rendered.
     */
    public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class, handlers ->
        (renderer, renderLevel) -> {
            for (Before handler : handlers) {
                handler.beforeGameRender(renderer, renderLevel);
            }
        });

    /**
     * An event triggered when the game has been rendered.
     */
    public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class, handlers ->
        (renderer, renderLevel) -> {
            for (After handler : handlers) {
                handler.afterGameRender(renderer, renderLevel);
            }
        });

    @FunctionalInterface
    public interface Before {
        void beforeGameRender(GameRenderer renderer, boolean renderLevel);
    }

    @FunctionalInterface
    public interface After {
        void afterGameRender(GameRenderer renderer, boolean renderLevel);
    }
}
