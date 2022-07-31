package me.icanttellyou.mods.photomode;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import me.icanttellyou.mods.photomode.client.PhotoModeScreen;
import net.minecraft.text.TranslatableText;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PhotoMode implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
    }

    private void afterInitScreen(MinecraftClient client, Screen screen, int windowWidth, int windowHeight) {
        if (screen instanceof GameMenuScreen) {
            final List<ClickableWidget> buttons = Screens.getButtons(screen);

            buttons.add(new ButtonWidget(screen.width / 2 - 48, 8, 98, 20, new TranslatableText("gui.photomode"), (button) -> {
                client.setScreen(new PhotoModeScreen(Text.of("")));
            }));
        }
    }
}
