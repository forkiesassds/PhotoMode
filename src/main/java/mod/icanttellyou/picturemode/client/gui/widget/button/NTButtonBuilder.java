package mod.icanttellyou.picturemode.client.gui.widget.button;

import mod.icanttellyou.picturemode.mixin.TooltipAccessor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A builder for Nostalgic Tweaks button widgets.
 */
public class NTButtonBuilder implements AbstractButtonBuilder<NTButtonBuilder, Object> {
    private final Object buttonBuilder;
    private final Class<?> layoutBuilderClass;

    public NTButtonBuilder(Component text, Consumer<Object> onClick) {
        try {
            Class<?> buttonWidgetClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.button.ButtonWidget");
            Method createMethod = buttonWidgetClass.getMethod("create", Component.class);
            this.buttonBuilder = createMethod.invoke(null, text);
            this.layoutBuilderClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.dynamic.LayoutBuilder");

            Class<?> buttonBuilderClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.button.AbstractButtonMaker");
            Method onPressMethod = buttonBuilderClass.getMethod("onPress", Consumer.class);
            onPressMethod.invoke(this.buttonBuilder, onClick);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct Nostalgic Tweaks button builder.", e);
        }
    }

    @Override
    public NTButtonBuilder pos(int x, int y) {
        try {
            Method pos = this.layoutBuilderClass.getMethod("pos", int.class, int.class);
            pos.invoke(this.buttonBuilder, x, y);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }

    @Override
    public NTButtonBuilder width(int width) {
        try {
            Method widthMeth = this.layoutBuilderClass.getMethod("width", int.class);
            widthMeth.invoke(this.buttonBuilder, width);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }

    @Override
    public NTButtonBuilder size(int width, int height) {
        try {
            Method size = this.layoutBuilderClass.getMethod("size", int.class, int.class);
            size.invoke(this.buttonBuilder, width, height);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }

    @Override
    public NTButtonBuilder disableIf(BooleanSupplier callback) {
        Predicate<Object> predicate = o -> callback.getAsBoolean();

        try {
            Class<?> activeBuilderClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.dynamic.ActiveBuilder");
            Method disableIf = activeBuilderClass.getMethod("disableIf", Predicate.class);
            disableIf.invoke(this.buttonBuilder, predicate);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }

    @Override
    public NTButtonBuilder tooltip(@Nullable Tooltip tooltip) {
        if (tooltip == null)
            return this;

        Component component = ((TooltipAccessor) tooltip).getMessage();
        try {
            Class<?> tooltipBuilderClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.dynamic.TooltipBuilder");
            Method tooltipMeth = tooltipBuilderClass.getMethod("tooltip", Component.class);
            tooltipMeth.invoke(this.buttonBuilder, component);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }

    @Override
    public NTButtonBuilder createNarration(Button.CreateNarration createNarration) {
        return this;
    }

    @Override
    public Object build() {
        try {
            Class<?> builderClass = Class.forName("mod.adrenix.nostalgic.client.gui.widget.dynamic.SelfBuilder");
            Method build = builderClass.getMethod("build");
            return build.invoke(this.buttonBuilder);
        } catch (Exception e) {
            throw new RuntimeException("Reflection fail in NTButtonBuilder.", e);
        }
    }
}
