package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.PictureModeClient;
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
        this.addRenderableWidget(PictureModeClient.makePMButton(this.minecraft, this));
    }
}
