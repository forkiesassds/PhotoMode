//? if <26.3 {
package mod.icanttellyou.picturemode.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker(/*? >=1.21.2 {*/ "setPostEffect" /*? } else {*/ /*"loadEffect" *//*?}*/)
    void invokeSetPostEffect(Identifier postEffect);
}
//? }