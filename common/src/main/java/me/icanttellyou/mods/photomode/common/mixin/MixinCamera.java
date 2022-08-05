package me.icanttellyou.mods.photomode.common.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
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
    private void injectUpdate(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            thirdPerson = true;
            ((InvokerCamera) ((Camera)(Object)this)).invokeSetRotation(45.0f + 45.0f * ((PhotoModeScreen)client.currentScreen).cameraRotation, ((PhotoModeScreen)client.currentScreen).cameraTilt);
        }
    }
}


