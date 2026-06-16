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
public class RenderTargetBlitEvent extends Event {
    //? if >=26.2 {
    /*protected final com.mojang.blaze3d.systems.GpuSurface surface;
    protected final com.mojang.blaze3d.systems.CommandEncoder commandEncoder;
    protected final com.mojang.blaze3d.textures.GpuTextureView textureView;

    protected RenderTargetBlitEvent(
        com.mojang.blaze3d.systems.GpuSurface surface,
        com.mojang.blaze3d.systems.CommandEncoder commandEncoder,
        com.mojang.blaze3d.textures.GpuTextureView textureView
    ) {
        this.surface = surface;
        this.commandEncoder = commandEncoder;
        this.textureView = textureView;
    }

    public com.mojang.blaze3d.systems.GpuSurface getSurface() {
        return this.surface;
    }

    public com.mojang.blaze3d.systems.CommandEncoder getCommandEncoder() {
        return this.commandEncoder;
    }

    public com.mojang.blaze3d.textures.GpuTextureView getTextureView() {
        return this.textureView;
    }
    *///? } else {
    protected final com.mojang.blaze3d.pipeline.RenderTarget target;

    protected RenderTargetBlitEvent(com.mojang.blaze3d.pipeline.RenderTarget target) {
        this.target = target;
    }

    public com.mojang.blaze3d.pipeline.RenderTarget getTarget() {
        return this.target;
    }
    //? }

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
        //? if >=26.2 {
        /*public Pre(
            com.mojang.blaze3d.systems.GpuSurface surface,
            com.mojang.blaze3d.systems.CommandEncoder commandEncoder,
            com.mojang.blaze3d.textures.GpuTextureView textureView
        ) {
            super(surface, commandEncoder, textureView);
        }
        *///? } else {
        public Pre(com.mojang.blaze3d.pipeline.RenderTarget target) {
            super(target);
        }
        //? }
    }

    /**
     * {@link RenderTargetBlitEvent.Pre} is fired once per frame,
     * after the current frame is drawn using the screen blit function.
     * <p>
     * This event only fires on the physical client.
     */
    public static class Post extends RenderTargetBlitEvent {
        //? if >=26.2 {
        /*public Post(
            com.mojang.blaze3d.systems.GpuSurface surface,
            com.mojang.blaze3d.systems.CommandEncoder commandEncoder,
            com.mojang.blaze3d.textures.GpuTextureView textureView
        ) {
            super(surface, commandEncoder, textureView);
        }
        *///? } else {
        public Post(com.mojang.blaze3d.pipeline.RenderTarget target) {
            super(target);
        }
        //? }
    }
}
