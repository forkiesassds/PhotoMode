package me.icanttellyou.mods.photomode.client;

import me.icanttellyou.mods.photomode.mixin.AccessPostEffectPass;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.resource.ResourceFactory;

import java.io.IOException;

public class PhotoModePostEffectPass extends PostEffectPass {
    private final PhotoModeScreen photoModeScreen;

    public PhotoModePostEffectPass(PhotoModeScreen screen, ResourceFactory resourceFactory, String programName, Framebuffer input, Framebuffer output, boolean linear) throws IOException {
        super(resourceFactory, programName, input, output, linear);
        photoModeScreen = screen;
    }

    @Override
    @SuppressWarnings("resource")
    public void render(float time) {
        ((AccessPostEffectPass) this).photoMode$getProgram().getUniformByNameOrDummy("Intensity").set(photoModeScreen.shaderIntensity);
        super.render(time);
    }
}
