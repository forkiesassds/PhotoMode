package mod.icanttellyou.picturemode.client.image;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import mod.icanttellyou.picturemode.client.config.AbstractGUIOptionsProviderFactory;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.config.GUIOptionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ScreenshotHandler {
    public boolean waitingForFrame = false;

    public static class Settings implements AbstractGUIOptionsProviderFactory {
        public static final Codec<Settings> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("fixed_width", 0).forGetter(settings -> settings.fixedWidth),
                Codec.INT.optionalFieldOf("fixed_height", 0).forGetter(settings -> settings.fixedHeight),
                Codec.FLOAT.fieldOf("res_multiplier").forGetter(settings -> settings.resMultiplier)
            ).apply(instance, Settings::new));

        public int fixedWidth;
        public int fixedHeight;
        public float resMultiplier;

        public Settings(int fixedWidth, int fixedHeight, float resMultiplier) {
            this.fixedWidth = fixedWidth;
            this.fixedHeight = fixedHeight;
            this.resMultiplier = resMultiplier;
        }

        public int getWidth() {
            return this.fixedWidth != 0
                ? this.fixedWidth
                : Minecraft.getInstance().getWindow().getWidth();
        }

        public int getHeight() {
            return this.fixedHeight != 0
                ? this.fixedHeight
                : Minecraft.getInstance().getWindow().getHeight();
        }

        @Override
        public GUIOptionsProvider getGUIOptionsProvider() {
            return new GUIOptionsProvider() {
                @Override
                public void provide(YetAnotherConfigLib.Builder builder, ConfigCategory.Builder mainCategory) {
                    mainCategory.group(OptionGroup.createBuilder()
                        .name(ConfigHelper.getConfigText("screenshotSettings.name"))
                        .option(Option.<Integer>createBuilder()
                            .name(ConfigHelper.getConfigText("screenshotSettings.fixedWidth.name"))
                            .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.fixedWidth.desc")))
                            .binding(0,
                                () -> Settings.this.fixedWidth, newVal -> Settings.this.fixedWidth = newVal)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                .formatValue(i -> i == 0
                                    ? ConfigHelper.getConfigText("screenshotSettings.defaultRes")
                                    : Component.literal(String.valueOf(i))))
                            .build())
                        .option(Option.<Integer>createBuilder()
                            .name(ConfigHelper.getConfigText("screenshotSettings.fixedHeight.name"))
                            .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.fixedHeight.desc")))
                            .binding(0,
                                () -> Settings.this.fixedHeight, newVal -> Settings.this.fixedHeight = newVal)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                .formatValue(i -> i == 0
                                    ? ConfigHelper.getConfigText("screenshotSettings.defaultRes")
                                    : Component.literal(String.valueOf(i))))
                            .build())
                        .option(Option.<Float>createBuilder()
                            .name(ConfigHelper.getConfigText("screenshotSettings.resMultiplier.name"))
                            .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.resMultiplier.desc")))
                            .binding(1.0F,
                                () -> Settings.this.resMultiplier, newVal -> Settings.this.resMultiplier = newVal)
                            .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                .step(0.25F)
                                .range(0.25F, 8.0F)
                                .formatValue(i ->
                                    ConfigHelper.getConfigText("screenshotSettings.resMultiplier.display",
                                        i, (int) (Settings.this.getWidth() * i), (int) (Settings.this.getHeight() * i))))
                            .build())
                        .build());
                }
            };
        }
    }
}
