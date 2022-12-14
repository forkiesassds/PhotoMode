package me.icanttellyou.mods.photomode.fabric;

import me.icanttellyou.mods.photomode.common.client.PhotoModeScreen;
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

import java.util.List;

@Environment(EnvType.CLIENT)
public class PhotoMode implements ClientModInitializer {
    private static final List<String> usePauseScreenWorkarround = List.of(
        "NostalgicPauseScreen" //used by nt 2.0 old pause screen tweak
    );
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
    }

    private void afterInitScreen(MinecraftClient client, Screen screen, int windowWidth, int windowHeight) {
        if (screen instanceof GameMenuScreen || usePauseScreenWorkarround.contains(screen.getClass().getSimpleName())) {
            final List<ClickableWidget> buttons = Screens.getButtons(screen);

            buttons.add(ButtonWidget.builder(Text.translatable("gui.photomode"), (button) -> {
                client.setScreen(new PhotoModeScreen(Text.of("")));
            }).position(screen.width / 2 - 48, 8).width(98).build());
        }
    }
}
