package mod.icanttellyou.picturemode.forgelike.client.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
//? if neoforge {
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
//? } else {
/*import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
*///? }

/**
 * Base class of the two Render Target blit events.
 * <p>
 * These events can be used to control the drawing of the main Render Target.
 * <p>
 *
 * @see RenderTargetBlitEvent.Pre
 * @see RenderTargetBlitEvent.Post
 */
public class RenderTargetBlitEvent extends Event {
    protected final RenderTarget target;

    protected RenderTargetBlitEvent(RenderTarget target) {
        this.target = target;
    }

    public RenderTarget getTarget() {
        return this.target;
    }

    /**
     * {@link RenderTargetBlitEvent.Pre} is fired once per frame,
     * before the current frame is drawn via {@link RenderTarget#blitToScreen}.
     * <p>
     * Cancelling this event will result in {@link RenderTarget#blitToScreen} not being called,
     * and the Render Target will not be drawn to the screen.
     * <p>
     * This event only fires on the physical client.
     */
    //? if forge
    //@Cancelable
    public static class Pre extends RenderTargetBlitEvent /*? if neoforge {*/ implements ICancellableEvent /*?}*/ {
        public Pre(RenderTarget target) {
            super(target);
        }
    }

    /**
     * {@link RenderTargetBlitEvent.Pre} is fired once per frame,
     * after the current frame is drawn via {@link RenderTarget#blitToScreen}.
     * <p>
     * This event only fires on the physical client.
     */
    public static class Post extends RenderTargetBlitEvent {
        public Post(RenderTarget target) {
            super(target);
        }
    }
}
