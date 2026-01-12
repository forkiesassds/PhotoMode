package mod.icanttellyou.photomode.value;

import mod.icanttellyou.photomode.util.Tickable;
import net.minecraft.util.Mth;

/**
 * This class is for an interpolated value.
 * <p>
 * Interpolation has to run each game tick for it to function.
 */
public class InterpolatedValue implements Value, Tickable {
    private final int duration;

    private double goal;
    private double start;

    private int progress;

    public InterpolatedValue(int duration) {
        this(0.0D, duration);
    }

    public InterpolatedValue(double def, int duration) {
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
        this.start = this.goal;
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

        this.progress = this.duration;
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
        double position = Mth.clamp((double) progress / duration + delta, 0.0D, 1.0D);
        return start + (goal - start) * position;
    }

    /**
     * The callback to run on game tick
     */
    @Override
    public void onTick() {
        if (progress < duration)
            progress++;
    }
}
