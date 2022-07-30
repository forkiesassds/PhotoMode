package me.icanttellyou.mods.photomode.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private boolean thirdPerson;
    MinecraftClient client = MinecraftClient.getInstance();

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void injectUpdate(Args args) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            args.set(0, 45.0f + 45.0f * ((PhotoModeScreen)client.currentScreen).cameraRotation);
            args.set(1, ((PhotoModeScreen)client.currentScreen).cameraTilt);
        }
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void injectUpdate2(CallbackInfo info) {
        if (client.currentScreen instanceof PhotoModeScreen) {
            thirdPerson = true;
        }
    }
}