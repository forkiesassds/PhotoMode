package me.icanttellyou.mods.photomode.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Fog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {
    @ModifyVariable(method = "setShaderFog", at = @At(value = "HEAD"), argsOnly = true)
    private static Fog photomode$injectSetShaderFog(Fog value) {
        MinecraftClient client = MinecraftClient.getInstance();
        float start = value.start();
        float end = value.end();

        if (client.currentScreen instanceof PhotoModeScreen) {
            float fogModifier = ((PhotoModeScreen) client.currentScreen).getFog(client.getRenderTickCounter().getTickDelta(true));
            if (value.start() != Float.MAX_VALUE) start *= fogModifier;
            end *= fogModifier;

            value = new Fog(start, end, value.shape(), value.red(), value.green(), value.blue(), value.alpha());
        }
        return value;
    }
}
