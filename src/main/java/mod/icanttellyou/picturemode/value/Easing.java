package mod.icanttellyou.picturemode.value;

import net.minecraft.util.Mth;

public enum Easing {
    LINEAR((start, goal, progress, duration, delta) -> {
        double position = Mth.clamp((progress + delta) / duration, 0.0D, 1.0D);
        return start + (goal - start) * position;
    });

    private final Function function;

    Easing(Function function) {
        this.function = function;
    }

    public double apply(double start, double goal, int progress, double duration, double delta) {
        return function.apply(start, goal, progress, duration, delta);
    }

    @FunctionalInterface
    private interface Function {
        double apply(double start, double goal, int progress, double duration, double delta);
    }
}
