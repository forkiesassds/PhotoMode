package mod.icanttellyou.picturemode.client.gui.widget.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public interface AbstractButtonBuilder<TBuilder extends AbstractButtonBuilder<TBuilder, TButton>, TButton> {
    static AbstractButtonBuilder<?, ?> getBuilder(Screen screen, Component component, Consumer<Object> onClick) {
        try {
            if (Class.forName("mod.adrenix.nostalgic.client.gui.screen.DynamicScreen")
                    .isAssignableFrom(screen.getClass())) {
                return new NTButtonBuilder(component, onClick);
            }
        } catch (Exception ignored) {}

        return new VanillaButtonBuilder(component, onClick::accept);
    }

    TBuilder pos(int x, int y);

    TBuilder width(int width);
    TBuilder size(int width, int height);

    default TBuilder bounds(int x, int y, int width, int height) {
        return this.pos(x, y).size(width, height);
    }

    TBuilder disableIf(BooleanSupplier callback);

    TBuilder tooltip(@Nullable Tooltip tooltip);
    TBuilder createNarration(Button.CreateNarration createNarration);
    TButton build();
}
