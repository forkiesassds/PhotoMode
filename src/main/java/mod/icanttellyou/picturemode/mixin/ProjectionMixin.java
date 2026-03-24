//? if >=26.1 {
/*package mod.icanttellyou.picturemode.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.icanttellyou.picturemode.imixin.PMPannableProjection;
import net.minecraft.client.renderer.Projection;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Projection.class)
public abstract class ProjectionMixin implements PMPannableProjection {
    @Shadow private boolean isMatrixDirty;

    @Unique private boolean pictureMode$hasPan;
    @Unique private float pictureMode$panX;
    @Unique private float pictureMode$panY;

    @WrapOperation(
        method = "getMatrix",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;set(Lorg/joml/Matrix4fc;)Lorg/joml/Matrix4f;"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/renderer/Projection;isMatrixDirty:Z",
                opcode = Opcodes.PUTFIELD
            )
        )
    )
    private Matrix4f addPanToMatrices(Matrix4f instance, Matrix4fc m, Operation<Matrix4f> original) {
        if (this.pictureMode$hasPan)
            m = ((Matrix4f) m).translate(this.pictureMode$panX, -this.pictureMode$panY, 0.0F);

        return original.call(instance, m);
    }

    @Override
    public void pictureMode$setPanX(float panX) {
        if (this.pictureMode$panX != panX) {
            this.isMatrixDirty = true;
            this.pictureMode$hasPan = panX != 0.0F;
            this.pictureMode$panX = panX;
        }
    }

    @Override
    public void pictureMode$setPanY(float panY) {
        if (this.pictureMode$panY != panY) {
            this.isMatrixDirty = true;
            this.pictureMode$hasPan = panY != 0.0F;
            this.pictureMode$panY = panY;
        }
    }
}
*///? }