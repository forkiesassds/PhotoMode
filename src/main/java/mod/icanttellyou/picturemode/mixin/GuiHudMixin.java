package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(/*? >=26.2 {*//*net.minecraft.client.gui.Gui.class*//*? } else {*/net.minecraft.client.renderer.GameRenderer.class/*? }*/)
public class GuiHudMixin {
    @Definition(id = "renderLevel", local = @Local(type = boolean.class, ordinal = 0, argsOnly = true))
    @Expression("renderLevel")
    @ModifyExpressionValue(
        //? if >=26.2 {
        //method = "extractRenderState",
        //? } else if >=26.1 {
        //method = "extractGui",
        //? } else {
        method = "render",
        //? }
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            ordinal = /*? >=26.1 {*/ /*0 *//*? } else {*/ 1 /*? }*/
        )
    )
    private boolean hideHudInPM(boolean original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null)
            return original;

        return !state.isEnabled() && original;
    }

    @WrapOperation(
        //? if >=26.2 {
        //method = "extractRenderState",
        //? } else if >=26.1 {
        //method = "extractGui",
        //? } else {
        method = "render",
        //? }
        at = @At(
            value = "INVOKE",
            //? if >=1.21.2 {
            target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            //? } else {
            //target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            //? }
        )
    )
    private void hideToastsInPM(@Coerce Object instance, GuiGraphics i, Operation<Void> original) {
        PictureModeState state = PictureModeClient.getState();

        if (state == null || !state.isEnabled())
            original.call(instance, i);
    }
}
