package mod.icanttellyou.picturemode.value;

/**
 * This interface provides methods for values and interpolation
 */
public interface Value {
    /**
     * Sets the goal of the value
     *
     * @param goal The new goal to set to
     */
    void setGoal(double goal);

    /**
     * Sets the goal of the value
     *
     * @param goal  The new goal to set to
     * @param delta The delta ticks for proper goal setting
     */
    default void setGoal(double goal, double delta) {
        setGoal(goal);
    }

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
