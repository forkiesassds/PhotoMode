package mod.icanttellyou.picturemode.client.gui.widget.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

/**
 * A wrapper for the vanilla button builder
 */
public class VanillaButtonBuilder implements AbstractButtonBuilder<VanillaButtonBuilder, Button> {
    private final Button.Builder builder;
    private BooleanSupplier toggleSupplier;

    public VanillaButtonBuilder(Component text, Button.OnPress onPress) {
        this.builder = new Button.Builder(text, button -> {
            if (toggleSupplier != null && !toggleSupplier.getAsBoolean())
                button.active = false;

            onPress.onPress(button);
        });
    }

    @Override
    public VanillaButtonBuilder pos(int x, int y) {
        builder.pos(x, y);
        return this;
    }

    @Override
    public VanillaButtonBuilder width(int width) {
        builder.width(width);
        return this;
    }

    @Override
    public VanillaButtonBuilder size(int width, int height) {
        builder.size(width, height);
        return this;
    }

    @Override
    public VanillaButtonBuilder disableIf(BooleanSupplier callback) {
        toggleSupplier = callback;
        return this;
    }

    @Override
    public VanillaButtonBuilder tooltip(@Nullable Tooltip tooltip) {
        builder.tooltip(tooltip);
        return this;
    }

    @Override
    public VanillaButtonBuilder createNarration(Button.CreateNarration createNarration) {
        builder.createNarration(createNarration);
        return this;
    }

    @Override
    public Button build() {
        Button button = builder.build();
        if (toggleSupplier != null)
            button.active = !toggleSupplier.getAsBoolean();

        return button;
    }
}
