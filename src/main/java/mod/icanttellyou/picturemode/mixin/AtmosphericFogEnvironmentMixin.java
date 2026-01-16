//? if >=1.21.6 {
package mod.icanttellyou.picturemode.mixin;

import mod.icanttellyou.picturemode.imixin.PMModifiableFog;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class AtmosphericFogEnvironmentMixin implements PMModifiableFog {
}
//? }