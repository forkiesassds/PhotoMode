package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private boolean thirdPerson;
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "update", at = @At(value = "TAIL"))
    private void injectUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            this.thirdPerson = ((PhotoModeScreen)client.currentScreen).playerVisible;
            ((InvokerCamera) ((Camera)(Object)this)).invokeSetRotation(45.0f + 45.0f * ((PhotoModeScreen)client.currentScreen).getRotation(tickDelta), ((PhotoModeScreen)client.currentScreen).getTilt(tickDelta));
        }
    }
}


