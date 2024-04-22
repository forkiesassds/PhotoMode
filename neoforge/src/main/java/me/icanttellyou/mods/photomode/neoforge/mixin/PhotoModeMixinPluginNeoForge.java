package me.icanttellyou.mods.photomode.neoforge.mixin;

import com.google.common.collect.ImmutableMap;
import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PhotoModeMixinPluginNeoForge implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "me.icanttellyou.mods.photomode.common.mixin.MixinWorldRendererVanilla", () -> {
                //do we have sodium?
                return FMLLoader.getLoadingModList().getMods().stream().anyMatch(
                        modInfo -> modInfo.getModId().equals("embeddium") || modInfo.getModId().equals("rubidium")
                                || modInfo.getModId().equals("sodium"));
            }
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        System.out.println( mixinClassName + " " + CONDITIONS.getOrDefault(mixinClassName, TRUE).get());
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
