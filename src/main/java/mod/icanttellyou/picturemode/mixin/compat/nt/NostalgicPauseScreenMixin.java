package mod.icanttellyou.picturemode.mixin.compat.nt;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "mod.adrenix.nostalgic.client.gui.screen.vanilla.pause.NostalgicPauseScreen", remap = false)
public abstract class NostalgicPauseScreenMixin extends Screen {
    protected NostalgicPauseScreenMixin(Component title) {
        super(title);
    }

    //TODO: maybe position the buttons in a way that would fit in more
    @Inject(method = "init", at = @At("TAIL"))
    private void addPMButtonToPauseMenu(CallbackInfo ci) {
        this.addRenderableWidget(PictureModeClient.makePMButton(this.minecraft, this));
    }
}
