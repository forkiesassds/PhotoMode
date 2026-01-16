package mod.icanttellyou.picturemode.value;

import mod.icanttellyou.picturemode.util.Tickable;
import net.minecraft.util.Mth;

public record ClampedValue(Value base, double min, double max) implements Value, Tickable {
    /**
     * Sets the goal of the value
     *
     * @param goal The new goal to set to
     */
    @Override
    public void setGoal(double goal) {
        setGoal(goal, 0.0D);
    }

    /**
     * Sets the goal of the value
     *
     * @param goal  The new goal to set to
     * @param delta The delta ticks for proper goal setting
     */
    @Override
    public void setGoal(double goal, double delta) {
        base.setGoal(Mth.clamp(goal, min, max), delta);
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
        double goal = base.getGoal();
        double added = goal + add;
        double clamped = added > max
            ? add - (added - max)
            : (added < min
                ? add - (min - added)
                : add);

        base.addToGoal(clamped, delta);
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
        double goal = base.getGoal();
        double subtracted = goal - subtract;
        double clamped = subtracted > max
            ? subtract - (subtracted - max)
            : (subtracted < min
                ? subtract - (min - subtracted)
                : subtract);

        base.subtractFromGoal(clamped, delta);
    }

    /**
     * Sets the direct value, rather than the goal
     *
     * @param value The new value to set
     */
    @Override
    public void setValue(double value) {
        base.setValue(value);
    }

    /**
     * Gets the goal of the value
     *
     * @return The goal of the value
     */
    @Override
    public double getGoal() {
        return base.getGoal();
    }

    /**
     * Gets the resulting value
     *
     * @param delta The delta value used for interpolation
     * @return The value
     */
    @Override
    public double getValue(double delta) {
        return base.getValue(delta);
    }

    @Override
    public boolean onTick() {
        if (base instanceof Tickable tickable) {
            tickable.onTick();
            return true;
        }

        return false;
    }
}
