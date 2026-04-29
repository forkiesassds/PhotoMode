package mod.icanttellyou.picturemode.forgelike;

import mod.icanttellyou.picturemode.PictureMode;
//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
//? } else {
/*import mod.icanttellyou.picturemode.forgelike.client.PictureModeForgelikeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
*///? }

@Mod(PictureMode.MOD_ID)
public class PictureModeForgelike {
    public PictureModeForgelike(/*? neoforge {*/ IEventBus modBus /*?}*/) {
        //? if forge {
        /*IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        //noinspection InstantiationOfUtilityClass
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new PictureModeForgelikeClient(modBus));
        *///? }
    }
}
