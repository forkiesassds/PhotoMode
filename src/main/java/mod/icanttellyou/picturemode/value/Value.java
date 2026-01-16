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
     * Adds a number to the current goal
     *
     * @param add The number to add
     */
    default void addToGoal(double add) {
        double curGoal = getGoal();
        setGoal(curGoal + add);
    }

    /**
     * Adds a number to the current goal
     *
     * @param add   The number to add
     * @param delta The delta ticks for proper goal setting
     */
    default void addToGoal(double add, double delta) {
        addToGoal(add);
    }

    /**
     * Subtracts a number from the current goal
     *
     * @param subtract The number to subtract
     */
    default void subtractFromGoal(double subtract) {
        double curGoal = getGoal();
        setGoal(curGoal - subtract);
    }

    /**
     * Subtracts a number from the current goal
     *
     * @param subtract The number to subtract
     * @param delta    The delta ticks for proper goal setting
     */
    default void subtractFromGoal(double subtract, double delta) {
        subtractFromGoal(subtract);
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

    /**
     * Wraps this value to a clamped value
     *
     * @param min The minimum possible value
     * @param max The maximum possible value
     * @return The value wrapped to be clamped
     */
    default Value clamped(double min, double max) {
        return new ClampedValue(this, min, max);
    }
}
