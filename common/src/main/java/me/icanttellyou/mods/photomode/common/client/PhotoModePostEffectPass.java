package me.icanttellyou.mods.photomode.common.client;

import me.icanttellyou.mods.photomode.common.mixin.AccessPostEffectPass;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;

public class PhotoModePostEffectPass extends PostEffectPass {
    private final PhotoModeScreen photoModeScreen;

    public PhotoModePostEffectPass(PhotoModeScreen screen, ResourceManager resourceManager, String programName, Framebuffer input, Framebuffer output) throws IOException {
        super(resourceManager, programName, input, output);
        photoModeScreen = screen;
    }

    @Override
    @SuppressWarnings("resource")
    public void render(float time) {
        ((AccessPostEffectPass) this).photoMode$getProgram().getUniformByNameOrDummy("Intensity").set(photoModeScreen.shaderIntensity);
        super.render(time);
    }
}
