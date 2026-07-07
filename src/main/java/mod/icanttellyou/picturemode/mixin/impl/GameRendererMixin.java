//? if >=26.3 {
/*package mod.icanttellyou.picturemode.mixin.impl;

import mod.icanttellyou.picturemode.imixin.PMPostEffectApplier;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements PMPostEffectApplier {
    @Shadow @Final private List<Identifier> requestedPostEffects;

    @Unique private Identifier pm$postEffect;
    @Unique private boolean pm$postEffectActive;

    @Inject(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    private void addPostEffect(CallbackInfo ci) {
        if (this.pm$postEffect != null && this.pm$postEffectActive) {
            this.requestedPostEffects.add(this.pm$postEffect);
        }
    }

    @Override
    public void pm$setPostEffect(Identifier id) {
        this.pm$postEffect = id;
        this.pm$postEffectActive = true;
    }

    @Override
    public void pm$clearPostEffect() {
        this.pm$postEffect = null;
        this.pm$postEffectActive = false;
    }
}
*///? }