package mod.icanttellyou.picturemode.client.image.screenshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import mod.icanttellyou.picturemode.client.config.AbstractGUIOptionsProviderFactory;
import mod.icanttellyou.picturemode.client.config.ConfigHelper;
import mod.icanttellyou.picturemode.client.config.GUIOptionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ScreenshotSettings implements AbstractGUIOptionsProviderFactory {
    public static final Codec<ScreenshotSettings> CODEC =
        RecordCodecBuilder.<ScreenshotSettings>create(instance -> instance.group(
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("fixed_width", 0)
                    .forGetter(settings -> settings.fixedWidth),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("fixed_height", 0)
                    .forGetter(settings -> settings.fixedHeight),
            Codec.FLOAT.fieldOf("res_multiplier").forGetter(settings -> settings.resMultiplier),
            Codec.INT.optionalFieldOf("frame_delay", 5)
                    .forGetter(settings -> settings.frameDelay)
        ).apply(instance, ScreenshotSettings::new))
            .flatXmap(ScreenshotSettings::validateWidthHeight, ScreenshotSettings::validateWidthHeight);

    public int fixedWidth;
    public int fixedHeight;
    public float resMultiplier;
    public int frameDelay;

    public ScreenshotSettings(int fixedWidth, int fixedHeight, float resMultiplier, int frameDelay) {
        this.fixedWidth = fixedWidth;
        this.fixedHeight = fixedHeight;
        this.resMultiplier = resMultiplier;
        this.frameDelay = frameDelay;
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

    private static DataResult<ScreenshotSettings> validateWidthHeight(ScreenshotSettings settings) {
        long multiplied = (long) settings.fixedWidth * settings.fixedHeight;
        if (multiplied >= Integer.MAX_VALUE)
            return DataResult.error(() -> "Fixed width and height are too high! Exceeding 32-bit integer limit in size!");

        return DataResult.success(settings);
    }

    /**
     * Gets the config GUI options provider
     *
     * @return The config GUI options provider for the format
     */
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
                            () -> ScreenshotSettings.this.fixedWidth, newVal -> ScreenshotSettings.this.fixedWidth = newVal)
                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                            .formatValue(i -> i == 0
                                ? ConfigHelper.getConfigText("screenshotSettings.defaultRes")
                                : Component.literal(String.valueOf(i))))
                        .build())
                    .option(Option.<Integer>createBuilder()
                        .name(ConfigHelper.getConfigText("screenshotSettings.fixedHeight.name"))
                        .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.fixedHeight.desc")))
                        .binding(0,
                            () -> ScreenshotSettings.this.fixedHeight, newVal -> ScreenshotSettings.this.fixedHeight = newVal)
                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                            .formatValue(i -> i == 0
                                ? ConfigHelper.getConfigText("screenshotSettings.defaultRes")
                                : Component.literal(String.valueOf(i))))
                        .build())
                    .option(Option.<Float>createBuilder()
                        .name(ConfigHelper.getConfigText("screenshotSettings.resMultiplier.name"))
                        .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.resMultiplier.desc")))
                        .binding(1.0F,
                            () -> ScreenshotSettings.this.resMultiplier, newVal -> ScreenshotSettings.this.resMultiplier = newVal)
                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .step(0.25F)
                            .range(0.25F, 8.0F)
                            .formatValue(i ->
                                ConfigHelper.getConfigText("screenshotSettings.resMultiplier.display",
                                        i, (int) (ScreenshotSettings.this.getWidth() * i), (int) (ScreenshotSettings.this.getHeight() * i))))
                        .build())
                    .option(Option.<Integer>createBuilder()
                        .name(ConfigHelper.getConfigText("screenshotSettings.frameDelay.name"))
                        .description(OptionDescription.of(ConfigHelper.getConfigText("screenshotSettings.frameDelay.desc")))
                        .binding(5,
                            () -> ScreenshotSettings.this.frameDelay, newVal -> ScreenshotSettings.this.frameDelay = newVal)
                        .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 50).step(1))
                        .build())
                    .build());
            }
        };
    }
}
