package mod.icanttellyou.picturemode.mixin.compat.nt;

import mod.icanttellyou.picturemode.PictureMode;
import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
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
        Button pmButton = Button.builder(Component.translatable("gui.picturemode"), button ->
                this.minecraft.setScreen(new PictureModeScreen(this, Component.literal(""))))
            .pos(width / 2 - 48, 8)
            .width(98)
            .build();

        Level level = this.minecraft.level;
        boolean disabled = level.dimensionTypeRegistration().is(TagKey.create(Registries.DIMENSION_TYPE,
            PictureMode.createId("disabled")));

        pmButton.active = !disabled;
        this.addRenderableWidget(pmButton);
    }
}
