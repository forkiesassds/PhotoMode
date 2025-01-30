package me.icanttellyou.mods.photomode;

import dev.architectury.injectables.annotations.ExpectPlatform;

@SuppressWarnings("unused")
public final class PhotoModeEnvironment {
    @ExpectPlatform
    public static boolean isModInstalled(String id) {
        throw new AssertionError();
    }
}
