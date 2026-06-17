package mod.icanttellyou.picturemode.mixin;

import com.google.common.collect.ImmutableMap;
import mod.icanttellyou.picturemode.client.ModStatus;
import mod.icanttellyou.picturemode.services.PictureModeServices;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PictureModeMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.ofEntries(
        //? if >=1.21.9
        Map.entry("mod.icanttellyou.picturemode.mixin.compat.journeymap.DebugEntryMixin", () -> ModStatus.HAS_JOURNEYMAP),
        Map.entry("mod.icanttellyou.picturemode.mixin.compat.journeymap.HudOverlayHandlerMixin", () -> ModStatus.HAS_JOURNEYMAP),
        //? if <=1.21.1
        //Map.entry("mod.icanttellyou.picturemode.mixin.compat.sodium.SodiumWorldRendererMixin", () -> ModStatus.HAS_SODIUM),
        Map.entry("mod.icanttellyou.picturemode.mixin.compat.sodium.RenderSectionManagerMixin", () -> ModStatus.HAS_SODIUM),
        Map.entry("mod.icanttellyou.picturemode.mixin.compat.sodium.DefaultChunkRendererMixin", () -> ModStatus.HAS_SODIUM),
        Map.entry("mod.icanttellyou.picturemode.mixin.compat.nt.PauseWidgetsMixin", () -> ModStatus.HAS_NOSTALGIC_TWEAKS),
        Map.entry("mod.icanttellyou.picturemode.mixin.LevelRendererMixinVanilla", () -> !ModStatus.HAS_SODIUM)
    );

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return CONDITIONS.getOrDefault(s1, TRUE).get();
    }

    @Override
    public void onLoad(String s) {}

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
