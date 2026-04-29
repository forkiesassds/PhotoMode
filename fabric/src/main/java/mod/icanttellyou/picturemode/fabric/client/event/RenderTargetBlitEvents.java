package mod.icanttellyou.picturemode.fabric.client.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Mods should use these events to control the drawing of the main Render Target
 * without adding injections into the game tick methods.
 * <p>
 * There are events for before and after the main Render Target is drawn to the screen.
 */
public final class RenderTargetBlitEvents {
    /**
     * An event triggered when the main Render Target is about to be drawn to the screen.
     * Mods can use this to prevent drawing graphics to the screen.
     *
     * <p>If a listener returned {@code false}, the Render Target will not be drawn to the screen,
     * the remaining listeners will not be called (if any), and {@link #AFTER}
     * event will not be triggered.
     */
    public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class, handlers -> target -> {
        for (Before handler : handlers) {
            if (!handler.beforeTargetBlit(target)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event triggered when the main Render Target has been drawn to the screen.
     *
     * <p>If the {@link #BEFORE} event was cancelled,
     * this event will not be triggered following the {@link #BEFORE} event
     */
    public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class, handlers -> target -> {
        for (After handler : handlers) {
            handler.afterTargetBlit(target);
        }
    });

    @FunctionalInterface
    public interface Before {
        boolean beforeTargetBlit(RenderTarget target);
    }

    @FunctionalInterface
    public interface After {
        void afterTargetBlit(RenderTarget target);
    }
}
