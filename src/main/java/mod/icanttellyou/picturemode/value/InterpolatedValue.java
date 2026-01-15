package mod.icanttellyou.picturemode.value;

import mod.icanttellyou.picturemode.util.Tickable;
import net.minecraft.util.Mth;

/**
 * This class is for an interpolated value.
 * <p>
 * Interpolation has to run each game tick for it to function.
 */
public class InterpolatedValue implements Value, Tickable {
    private final double duration;
    private final Easing easing;

    private double goal;
    private double start;

    private int progress;
    private double curDuration;

    public InterpolatedValue(Easing easing, double duration) {
        this(easing, 0.0D, duration);
    }

    public InterpolatedValue(Easing easing, double def, double duration) {
        if (duration == 0.0D)
            throw new IllegalArgumentException("Cannot use duration of 0! Please use StaticValue instead!");

        this.easing = easing;
        this.progress = (int) Math.ceil(duration);
        this.duration = duration;
        this.curDuration = duration;
        this.setValue(def);
    }

    /**
     * Sets the goal of the value
     *
     * @param goal The new goal to set to
     */
    @Override
    public void setGoal(double goal) {
        this.setGoal(goal, 0.0D);
    }

    /**
     * Sets the goal of the value
     *
     * @param goal  The new goal to set to
     * @param delta The delta ticks for proper goal setting
     */
    @Override
    public void setGoal(double goal, double delta) {
        if (this.progress >= this.curDuration) {
            this.curDuration = this.duration;
            this.start = this.goal;
        } else {
            double curValue = this.getValue(delta);
            //int direction = goal - curValue >= 0 ? 1 : -1;

            this.start = curValue;
            this.curDuration = this.duration /*+ (this.progress + delta) * direction*/;
        }

        this.goal = goal;
        this.progress = 0;
    }

    /**
     * Sets the direct value, rather than the goal
     *
     * @param value The new value to set
     */
    @Override
    public void setValue(double value) {
        this.goal = value;
        this.start = value;

        this.curDuration = this.duration;
        this.progress = (int) this.duration;
    }

    /**
     * Gets the goal of the value
     *
     * @return The goal of the value
     */
    @Override
    public double getGoal() {
        return this.goal;
    }

    /**
     * Gets the resulting value
     *
     * @param delta The delta value used for interpolation
     * @return The value
     */
    @Override
    public double getValue(double delta) {
        double position = Mth.clamp((progress + delta) / curDuration, 0.0D, 1.0D);
        return easing.apply(position, start, goal);
    }

    @Override
    public void onTick() {
        if (progress < curDuration)
            progress++;
    }
}
