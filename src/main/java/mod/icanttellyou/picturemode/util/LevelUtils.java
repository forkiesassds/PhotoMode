package mod.icanttellyou.picturemode.util;

import mod.icanttellyou.picturemode.PictureMode;
//? if >=1.21.11 {
import net.minecraft.core.Registry;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;
//? }
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

public class LevelUtils {
    /**
     * Gets the length of a day in the specified level
     *
     * @param level The level to get length of a day from
     * @return The duration of a day
     */
    public static int getDayLength(Level level) {
        //? if >=1.21.11 {
        Registry<Timeline> timelines = level.registryAccess().lookupOrThrow(Registries.TIMELINE);
        Timeline dayTimeline = timelines.getValueOrThrow(Timelines.DAY);

        return dayTimeline.periodTicks().orElseThrow();
        //? } else {
        //return 24000;
        //? }
    }

    /**
     * Checks if Picture Mode is disabled in the given level
     *
     * @param level The level to check
     * @return Whether Picture Mode is disabled in the level or not.
     */
    public static boolean isPMDisabledForDimension(Level level) {
        if (level == null)
            return false;

        return level.dimensionTypeRegistration().is(TagKey.create(Registries.DIMENSION_TYPE,
            PictureMode.createId("disabled")));
    }

}
