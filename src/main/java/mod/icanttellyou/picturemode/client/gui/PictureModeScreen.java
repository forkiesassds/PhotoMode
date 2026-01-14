package mod.icanttellyou.picturemode.client.gui;

import mod.icanttellyou.picturemode.PictureModeConstants;
import mod.icanttellyou.picturemode.client.PictureModeClient;
import mod.icanttellyou.picturemode.client.PictureModeState;
import mod.icanttellyou.picturemode.client.gui.widget.Slider;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PictureModeScreen extends Screen {
    private static final String DEFAULT_KEY = "gui.picturemode.default";
    private static final String DEGREES_KEY = "gui.picturemode.degrees";

    private static final String TIME_KEY = "gui.picturemode.time";
    private static final String FOG_KEY = "gui.picturemode.fog";
    private static final String TILT_KEY = "gui.picturemode.tilt";

    private final PictureModeState pmState;

    protected PictureModeScreen(Component title) {
        super(title);
        this.pmState = PictureModeClient.getState();
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new Slider(width - 150, 0, 0.0D,
            (slider, value, messageUpdate) -> {
                //TODO: time slider
            }));
        addRenderableWidget(new Slider(width - 150, 0, 1.0D,
            (slider, value, messageUpdate) -> {
                int percent = (int) (value * 100.0D);
                slider.setMessage(Component.translatable(FOG_KEY, percent));

                if (!messageUpdate)
                    pmState.fog.setGoal(Math.pow(2.0D, 8.0D * value - 8.0D));
            }));
        addRenderableWidget(new Slider(width - 150, 0, PictureModeConstants.DEFAULT_TILT / 90.0D,
            (slider, value, messageUpdate) -> {
                int degrees = (int) (value * 90.0D);
                slider.setMessage(Component.translatable(TILT_KEY, degrees == 30
                        ? Component.translatable(DEFAULT_KEY)
                        : Component.translatable(DEGREES_KEY, degrees)));

                if (!messageUpdate)
                    pmState.cameraTilt.setGoal(value * 90.0D);
            }));
    }

}
