package me.icanttellyou.mods.photomode.forge;

import dev.architectury.hooks.client.screen.ScreenHooks;
import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("photomode")
public class PhotoMode {
    MinecraftClient client = MinecraftClient.getInstance();
    public PhotoMode() {
        MinecraftForge.EVENT_BUS.addListener(this::screenEventHandler);
    }

    private void screenEventHandler(ScreenEvent event) {
        Screen screen = event.getScreen();
        if (screen instanceof GameMenuScreen) {
            ScreenHooks.addRenderableWidget(screen, new ButtonWidget(screen.width / 2 - 48, 8, 98, 20, Text.translatable("gui.photomode"), (button) -> {
                client.setScreen(new PhotoModeScreen(Text.of("")));
            }));
        }
    }
}
