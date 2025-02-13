package me.icanttellyou.mods.photomode.neoforge;

import net.neoforged.fml.loading.FMLLoader;

@SuppressWarnings("unused")
public class PhotoModeEnvironmentImpl {
    public static boolean isModInstalled(String id) {
        return FMLLoader.getLoadingModList().getModFileById(id) != null;
    }
}
