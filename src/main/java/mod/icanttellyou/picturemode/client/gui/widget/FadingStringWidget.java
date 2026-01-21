package mod.icanttellyou.picturemode.client.gui.widget;

import mod.icanttellyou.picturemode.util.Tickable;
import mod.icanttellyou.picturemode.value.Easing;
import mod.icanttellyou.picturemode.value.InterpolatedValue;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FadingStringWidget extends AbstractStringWidget implements Tickable {
    private static final int TEXT_MARGIN = 2;

    private int maxWidth = 0;
    private int cachedWidth = 0;
    private boolean cachedWidthDirty = true;

    private TextOverflow textOverflow = TextOverflow.CLAMPED;

    private final InterpolatedValue alphaFade = new InterpolatedValue(Easing.LINEAR, 1.0D, (double) SharedConstants.TICKS_PER_SECOND / 2);
    private boolean wasMouseOver = false;
    private boolean haltFading;
    private final int defaultTicksUntilFade;
    private int ticksUntilFade;

    @Nullable
    private Consumer<Style> componentClickHandler = null;

    public FadingStringWidget(int x, int y, int width, int height, Component message, Font font, int ticksUntilFade, boolean haltFading) {
        super(x, y, width, height, message, font);

        this.defaultTicksUntilFade = ticksUntilFade;
        this.ticksUntilFade = ticksUntilFade;
        this.haltFading = haltFading;
    }

    public FadingStringWidget(int width, int height, Component message, Font font, int ticksUntilFade, boolean haltFading) {
        this(0, 0, width, height, message, font, ticksUntilFade, haltFading);
    }

    public FadingStringWidget(Component message, Font font, int ticksUntilFade, boolean haltFading) {
        this(0, 0, font.width(message.getVisualOrderText()), 9, message, font, ticksUntilFade, haltFading);
    }

    @Override
    public void setMessage(Component message) {
        super.setMessage(message);
        //? if <1.21.11
        //this.setWidth(this.getFont().width(message.getVisualOrderText()));
        this.cachedWidthDirty = true;
        this.haltFading = false;
        this.resetFade();
    }

    public FadingStringWidget setMaxWidth(int maxWidth) {
        return this.setMaxWidth(maxWidth, TextOverflow.CLAMPED);
    }

    public FadingStringWidget setMaxWidth(int maxWidth, TextOverflow textOverflow) {
        this.maxWidth = maxWidth;
        this.textOverflow = textOverflow;
        return this;
    }

    private void resetFade() {
        if (this.haltFading)
            return;

        this.visible = true;
        this.alphaFade.setValue(1.0D);
        this.ticksUntilFade = this.defaultTicksUntilFade;
    }

    public void fadeOut() {
        this.haltFading = false;
        this.alphaFade.setGoal(0.0D);
        this.ticksUntilFade = -1;
    }

    @Override
    public int getWidth() {
        if (this.maxWidth > 0) {
            if (this.cachedWidthDirty) {
                this.cachedWidth = Math.min(this.maxWidth, this.getFont().width(this.getMessage().getVisualOrderText()));
                this.cachedWidthDirty = false;
            }

            return this.cachedWidth;
        } else {
            return super.getWidth();
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
        double fade = this.alphaFade.getValue(deltaTicks);
        if (fade == 0.0D) {
            this.visible = false;
            return;
        }

        if (this.isMouseOver(mouseX, mouseY)) {
            this.resetFade();
            this.haltFading = true;
            this.wasMouseOver = true;
        } else if (this.wasMouseOver) {
            this.haltFading = false;
            this.wasMouseOver = false;
        }

        //? if >=1.21.11 {
        this.alpha = (float) fade;
        super.renderWidget(graphics, mouseX, mouseY, deltaTicks);
        //? } else {
        /*this.renderText(graphics, fade);
        *///? }
    }

    //? if >=1.21.11 {
    @Override
    public void visitLines(final net.minecraft.client.gui.ActiveTextCollector output) {
    //? } else {
    /*public void renderText(GuiGraphics graphics, double alpha) {
    *///? }
        Component message = this.getMessage();
        Font font = this.getFont();
        int maxWidth = this.maxWidth > 0 ? this.maxWidth : this.getWidth();
        int textWidth = font.width(message);
        int x = this.getX();
        int y = this.getY() + (this.getHeight() - 9) / 2;

        //? if <1.21.11 {
        /*if (alpha == 0.0D)
            return;

        int textColor = net.minecraft.util.Mth.floor(alpha * 255.0D) << 24 | 0xFFFFFF;
        *///? }

        if (textWidth > maxWidth) {
            switch (this.textOverflow) {
                case CLAMPED:
                    //? if >=1.21.11 {
                    output.accept(x, y, clipText(message, font, maxWidth));
                    //? } else {
                    /*graphics.drawString(font, clipText(message, font, maxWidth), x, y, textColor);
                    *///? }
                    break;
                case SCROLLING:
                    int minX = x + TEXT_MARGIN;
                    int maxX = x + maxWidth - TEXT_MARGIN;
                    int minY = this.getY();
                    int maxY = this.getY() + this.getHeight();

                    //? if >=1.21.11 {
                    output.acceptScrolling(message, (minX + maxX) / 2, minX, maxX, minY, maxY);
                    //? } else {
                    /*renderScrollingString(
                        graphics,
                        font,
                        message,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        textColor
                    );
                    *///? }
            }
        } else {
            //? if >=1.21.11 {
            output.accept(x, y, message.getVisualOrderText());
            //? } else {
            /*graphics.drawString(font, message.getVisualOrderText(), x, y, textColor);
            *///? }
        }
    }

    //? if <1.21.11 {
    /*@Override
    //? if >=1.21.9 {
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean isDoubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
    //? } else {
    /^public boolean mouseClicked(double mouseX, double mouseY, int button) {
     ^///? }

        if (!this.visible || !this.isMouseOver(mouseX, mouseY))
            return false;

        Component message = this.getMessage();
        Font font = this.getFont();

        Style style = font.getSplitter().componentStyleAtWidth(message, net.minecraft.util.Mth.floor(mouseX - this.getX()));
        if (style != null && this.componentClickHandler != null) {
            this.componentClickHandler.accept(style);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setComponentClickHandler(@Nullable Consumer<Style> componentClickHandler) {
        this.componentClickHandler = componentClickHandler;
    }
    *///? }

    @Override
    public void playDownSound(SoundManager o) {}

    public static FormattedCharSequence clipText(Component component, Font font, int width) {
        FormattedText formattedText = font.substrByWidth(component, width - font.width(CommonComponents.ELLIPSIS));
        return Language.getInstance().getVisualOrder(FormattedText.composite(formattedText, CommonComponents.ELLIPSIS));
    }

    @Override
    public boolean onTick() {
        if (this.haltFading)
            return true;

        if (this.ticksUntilFade > 0) {
            this.ticksUntilFade--;
            return true;
        } else if (this.ticksUntilFade == 0) {
            this.alphaFade.setGoal(0.0D);
            this.ticksUntilFade = -1;
        }

        return this.alphaFade.onTick();
    }

    public enum TextOverflow {
        CLAMPED,
        SCROLLING;
    }
}
