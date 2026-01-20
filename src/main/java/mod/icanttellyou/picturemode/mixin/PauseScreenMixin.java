package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.client.gui.PictureModeScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
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
        Button pmButton = Button.builder(Component.translatable("gui.picturemode"), button ->
                this.minecraft.setScreen(new PictureModeScreen(this, Component.literal(""))))
            .pos(width / 2 - 48, 8)
            .width(98)
            .build();

        Level level = this.minecraft.level;
        boolean disabled = level.dimensionTypeRegistration().is(TagKey.create(Registries.DIMENSION_TYPE,
                Identifier.tryParse("picturemode:disabled")));

        pmButton.active = !disabled;
        this.addRenderableWidget(pmButton);
    }
}
