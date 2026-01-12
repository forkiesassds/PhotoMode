package mod.icanttellyou.photomode.value;

/**
 * This class is for a static, uninterpolated value.
 * @param <N> The type of number to use
 */
public class StaticValue implements Value {
    private double number;

    /**
     * Sets the goal of the value
     *
     * @param goal The new goal to set to
     */
    @Override
    public void setGoal(double goal) {
        this.number = goal;
    }

    /**
     * Sets the direct value, rather than the goal
     *
     * @param value The new value to set
     */
    @Override
    public void setValue(double value) {
        this.number = value;
    }

    /**
     * Gets the goal of the value
     *
     * @return The goal of the value
     */
    @Override
    public double getGoal() {
        return this.number;
    }

    /**
     * Gets the resulting value
     *
     * @param delta The delta value used for interpolation
     * @return The value
     */
    @Override
    public double getValue(double delta) {
        return this.number;
    }
}
