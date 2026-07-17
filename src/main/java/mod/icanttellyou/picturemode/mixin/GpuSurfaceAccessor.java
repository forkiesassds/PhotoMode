//? if >=26.2 {
/*package mod.icanttellyou.picturemode.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//~ if >=26.3 'com.mojang.renderpearl.api.device.GpuSurface' -> 'com.mojang.renderpearl.frontend.FrontendGpuSurface'
@Mixin(com.mojang.renderpearl.api.device.GpuSurface.class)
public interface GpuSurfaceAccessor {
    @Accessor("hasImageAcquired")
    void setHasImageAcquired(boolean value);
}
*///? }