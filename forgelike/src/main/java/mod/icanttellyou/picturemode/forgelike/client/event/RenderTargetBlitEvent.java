package mod.icanttellyou.picturemode.forgelike.client.event;

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
//~ if >=26.2 'com.mojang.blaze3d.pipeline.RenderTarget' -> 'com.mojang.blaze3d.systems.GpuSurface' {
public class RenderTargetBlitEvent extends Event {
    protected final com.mojang.blaze3d.pipeline.RenderTarget target;

    protected RenderTargetBlitEvent(com.mojang.blaze3d.pipeline.RenderTarget target) {
        this.target = target;
    }

    public com.mojang.blaze3d.pipeline.RenderTarget getTarget() {
        return this.target;
    }

    /**
     * {@link RenderTargetBlitEvent.Pre} is fired once per frame,
     * before the current frame is drawn for presenting.
     * <p>
     * Cancelling this event will result in the screen blit function not being called,
     * and the Render Target will not be drawn to the screen.
     * <p>
     * This event only fires on the physical client.
     */
    //? if forge
    //@Cancelable
    public static class Pre extends RenderTargetBlitEvent /*? if neoforge {*/ implements ICancellableEvent /*?}*/ {
        public Pre(com.mojang.blaze3d.pipeline.RenderTarget target) {
            super(target);
        }
    }

    /**
     * {@link RenderTargetBlitEvent.Pre} is fired once per frame,
     * after the current frame is drawn using the screen blit function.
     * <p>
     * This event only fires on the physical client.
     */
    public static class Post extends RenderTargetBlitEvent {
        public Post(com.mojang.blaze3d.pipeline.RenderTarget target) {
            super(target);
        }
    }
}
//~ }