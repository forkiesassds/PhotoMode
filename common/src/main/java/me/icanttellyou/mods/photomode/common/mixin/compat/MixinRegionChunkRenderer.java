package me.icanttellyou.mods.photomode.common.mixin.compat;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import me.jellysquid.mods.sodium.client.render.chunk.RegionChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RegionChunkRenderer.class)
public class MixinRegionChunkRenderer {
    @ModifyVariable(method = "buildDrawBatches", at = @At("STORE"), remap = false)
    private ChunkRenderBounds injectBuildDrawBatches(ChunkRenderBounds chunkRenderBounds) {
        if (MinecraftClient.getInstance().currentScreen instanceof PhotoModeScreen) {
            return new ChunkRenderBounds(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
                    Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        }
        return chunkRenderBounds;
    }
}
