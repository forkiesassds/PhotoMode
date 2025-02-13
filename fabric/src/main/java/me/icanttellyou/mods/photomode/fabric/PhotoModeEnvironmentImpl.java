package me.icanttellyou.mods.photomode.fabric;

import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class PhotoModeEnvironmentImpl {
    public static boolean isModInstalled(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
