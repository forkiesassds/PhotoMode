package mod.icanttellyou.picturemode.client.gui.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

@SuppressWarnings("DataFlowIssue")
public class Slider extends AbstractSliderButton {
    private final OnUpdate onUpdate;

    public Slider(int x, int y, Component message, double value) {
        this(x, y, 150, 20, message, value);
    }

    public Slider(int x, int y, double value, OnUpdate onUpdate) {
        this(x, y, 150, 20, value, onUpdate);
    }

    public Slider(int x, int y, int width, int height, Component message, double value) {
        this(x, y, width, height, value, (slider, val, update) -> slider.setMessage(message));
    }

    public Slider(int x, int y, int width, int height, double value, OnUpdate onUpdate) {
        super(x, y, width, height, null, value);
        this.onUpdate = onUpdate;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        onUpdate.onUpdate(this, value, true);
    }

    @Override
    protected void applyValue() {
        onUpdate.onUpdate(this, value, false);
    }

    @FunctionalInterface
    public interface OnUpdate {
        void onUpdate(Slider slider, double value, boolean messageUpdate);
    }
}
