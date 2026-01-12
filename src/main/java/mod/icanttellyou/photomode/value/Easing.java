package mod.icanttellyou.photomode.value;

import net.minecraft.util.Mth;

public enum Easing {
    LINEAR((start, goal, progress, duration, delta) -> {
        double position = Mth.clamp((double) progress / duration + delta, 0.0D, 1.0D);
        return start + (goal - start) * position;
    });

    private final Function function;

    Easing(Function function) {
        this.function = function;
    }

    public double apply(double start, double goal, int progress, int duration, double delta) {
        return function.apply(start, goal, progress, duration, delta);
    }

    @FunctionalInterface
    private interface Function {
        double apply(double start, double goal, int progress, int duration, double delta);
    }
}
