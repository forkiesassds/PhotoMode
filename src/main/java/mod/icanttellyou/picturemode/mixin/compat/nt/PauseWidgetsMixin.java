package mod.icanttellyou.picturemode.mixin.compat.nt;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "mod.adrenix.nostalgic.client.gui.screen.vanilla.pause.PauseWidgets", remap = false)
public class PauseWidgetsMixin {
    @Unique
    private Screen pm$pauseScreen;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void getScreenForAddingButtons(@Coerce Screen pauseScreen, CallbackInfo ci) {
        this.pm$pauseScreen = pauseScreen;
    }

    //TODO: maybe position the buttons in a way that would fit in more
    @Inject(method = "init", at = @At("HEAD"))
    private void addPMButtonToPauseMenu(CallbackInfo ci) {
        if (PictureModeClient.getConfig().buttonInPauseMenu) {
            Object button = PictureModeClient.makePMButton(Minecraft.getInstance(), this.pm$pauseScreen);
            pm$addWidget(pm$pauseScreen, button);
        }
    }

    @Unique
    private static void pm$addWidget(Object screen, Object widget) {
        try {
            Class<?> dynamicWidgetClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.dynamic.DynamicWidget");
            Class<?> dynamicScreenClass = Class.forName("mod.adrenix.nostalgic.client.gui.screen.DynamicScreen");
            Method addWidget = dynamicScreenClass.getMethod("addWidget", dynamicWidgetClass);
            addWidget.invoke(screen, widget);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add widget.", e);
        }
    }
}
