package mod.icanttellyou.picturemode.forgelike.client.event.listeners;

import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.keymap.PictureModeKeymaps;
import net.minecraft.client.Minecraft;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
//? if >=1.20.5 {
import net.neoforged.neoforge.client.event.ClientTickEvent;
//? } else {
//import net.neoforged.neoforge.event.TickEvent;
//? }
import net.neoforged.neoforge.common.util.Lazy;
//? } else {
/*import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
*///? }

import java.util.function.Consumer;

public final class GameTickEventListeners {
    private final Lazy<Consumer<Minecraft>> keymappingHandler = Lazy.of(PictureModeKeymaps::getMappingHandler);

    @SubscribeEvent
    public void onGameTickPre(
        //? if neoforge && >=1.20.5 {
        ClientTickEvent.Pre
        //? } else {
        //TickEvent.ClientTickEvent
        //? }
        event
    ) {
        //? if (forge && <1.21.1) || <1.20.5 {
        /*if (event.phase != TickEvent.Phase.START)
            return;
        *///? }

        Minecraft mc = Minecraft.getInstance();
        PictureModeState state = PictureModeClient.getState();

        if (mc.level == null || state == null)
            return;

        state.tick();
    }

    @SubscribeEvent
    public void onGameTickPost(
        //? if neoforge && >=1.20.5 {
        ClientTickEvent.Post
        //? } else {
        //TickEvent.ClientTickEvent
        //? }
        event
    ) {
        //? if (forge && <1.21.1) || <1.20.5 {
        /*if (event.phase != TickEvent.Phase.END)
            return;
        *///? }

        Minecraft mc = Minecraft.getInstance();
        keymappingHandler.get().accept(mc);
    }
}
