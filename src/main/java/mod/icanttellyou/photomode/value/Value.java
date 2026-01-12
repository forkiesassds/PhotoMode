package mod.icanttellyou.photomode.value;

/**
 * This interface provides methods for values and interpolation
 * @param <N> The type of number to use
 */
public interface Value {
    /**
     * Sets the goal of the value
     *
     * @param goal The new goal to set to
     */
    void setGoal(double goal);

    /**
     * Sets the direct value, rather than the goal
     *
     * @param value The new value to set
     */
    void setValue(double value);

    /**
     * Gets the goal of the value
     *
     * @return The goal of the value
     */
    double getGoal();

    /**
     * Gets the resulting value
     *
     * @param delta The delta value used for interpolation
     * @return The value
     */
    double getValue(double delta);
}
