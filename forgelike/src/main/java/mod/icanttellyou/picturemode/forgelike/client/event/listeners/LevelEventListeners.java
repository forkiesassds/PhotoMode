package mod.icanttellyou.picturemode.forgelike.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import net.minecraft.world.level.LevelAccessor;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
//? } else {
/*import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
*///? }

public final class LevelEventListeners {
    private LevelAccessor recentlyConstructedLevel = null;

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        this.recentlyConstructedLevel = event.getLevel();
        PictureModeClient.onWorldLoad();
    }

    @SubscribeEvent
    public void onLevelUnload(LevelEvent.Unload event) {
        if (this.recentlyConstructedLevel != event.getLevel())
            return;

        PictureModeClient.onWorldExit();
    }
}
