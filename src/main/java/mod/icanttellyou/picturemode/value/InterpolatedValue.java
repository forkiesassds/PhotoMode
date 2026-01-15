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

    public InterpolatedValue(Easing easing, double duration) {
        this(easing, 0.0D, duration);
    }

    public InterpolatedValue(Easing easing, double def, double duration) {
        if (duration == 0.0D)
            throw new IllegalArgumentException("Cannot use duration of 0! Please use StaticValue instead!");

        this.easing = easing;
        this.progress = (int) Math.ceil(duration);
        this.duration = duration;
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
        if (this.progress >= this.duration) {
            this.start = this.goal;
        } else {
            this.start = this.getValue(delta);
        }

        this.goal = goal;
        this.progress = 0;
    }

    /**
     * Adds a number to the current goal
     *
     * @param add The number to add
     */
    @Override
    public void addToGoal(double add) {
        addToGoal(add, 0.0D);
    }

    /**
     * Adds a number to the current goal
     *
     * @param add   The number to add
     * @param delta The delta ticks for proper goal setting
     */
    @Override
    public void addToGoal(double add, double delta) {
        if (this.progress >= this.duration) {
            this.start = this.goal;
        } else {
            this.start = this.getValue(delta);
        }

        this.goal += add;
        this.progress = 0;
    }

    /**
     * Subtracts a number from the current goal
     *
     * @param subtract The number to subtract
     */
    @Override
    public void subtractFromGoal(double subtract) {
        subtractFromGoal(subtract, 0.0D);
    }

    /**
     * Subtracts a number from the current goal
     *
     * @param subtract The number to subtract
     * @param delta    The delta ticks for proper goal setting
     */
    @Override
    public void subtractFromGoal(double subtract, double delta) {
        if (this.progress >= this.duration) {
            this.start = this.goal;
        } else {
            this.start = this.getValue(delta);
        }

        this.goal -= subtract;
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
        double position = Mth.clamp((progress + delta) / duration, 0.0D, 1.0D);
        return easing.apply(position, start, goal);
    }

    @Override
    public void onTick() {
        if (progress < duration)
            progress++;
    }
}
