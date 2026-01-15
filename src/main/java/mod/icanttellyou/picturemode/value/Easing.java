package mod.icanttellyou.picturemode.value;

import net.minecraft.util.Mth;

public enum Easing {
    LINEAR(Mth::lerp),
    CUBIC((delta, start, goal) ->
        Mth.lerp(3 * delta * delta - 2 * delta * delta * delta, start, goal)),
    EXPONENTIAL((delta, start, goal) ->
        Mth.lerp(1 - Math.exp(-delta * 5), start, goal));

    private final Function function;

    Easing(Function function) {
        this.function = function;
    }

    public double apply(double delta, double start, double goal) {
        return function.apply(delta, start, goal);
    }

    @FunctionalInterface
    private interface Function {
        double apply(double delta, double start, double goal);
    }
}
