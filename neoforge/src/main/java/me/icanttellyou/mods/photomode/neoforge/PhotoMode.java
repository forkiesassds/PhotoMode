package me.icanttellyou.mods.photomode.neoforge;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;

import java.util.List;

@Mod("photomode")
public class PhotoMode {
    MinecraftClient client = MinecraftClient.getInstance();
    private static final List<String> usePauseScreenWorkarround = List.of(
        "NostalgicPauseScreen" //used by nt 2.0 old pause screen tweak
    );
    public PhotoMode() {
        NeoForge.EVENT_BUS.addListener(this::screenEventHandler);
    }

    private void screenEventHandler(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof GameMenuScreen || usePauseScreenWorkarround.contains(screen.getClass().getSimpleName())) {
            screen.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.photomode"), (button) -> {
                client.setScreen(new PhotoModeScreen(Text.of("")));
            }).position(screen.width / 2 - 48, 8).width(98).build());
        }
    }
}
