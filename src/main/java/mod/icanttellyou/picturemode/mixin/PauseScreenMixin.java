package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "createPauseMenu", at = @At("TAIL"))
    private void addPMButtonToPauseMenu(CallbackInfo ci) {
        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.picturemode"), button ->
                minecraft.setScreen(new PictureModeScreen(this, Component.literal(""))))
            .pos(width / 2 - 48, 8)
            .width(98)
            .build()
        );
    }
}
