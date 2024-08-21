package me.icanttellyou.mods.photomode.common.client;

import com.google.gson.JsonSyntaxException;
import me.icanttellyou.mods.photomode.common.mixin.AccessPostEffectProcessor;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;

import java.io.IOException;

//public class PhotoModePostEffectProcessor extends PostEffectProcessor {
//    private static PhotoModeScreen photoModeScreen;
//
//    public PhotoModePostEffectProcessor(PhotoModeScreen screen, TextureManager textureManager, ResourceFactory resourceFactory, Framebuffer framebuffer, Identifier id) throws IOException, JsonSyntaxException {
//        super(hack(screen, textureManager), resourceFactory, framebuffer, id);
//    }
//
//    //AAAAAAAAAAAAAAAAAA
//    private static TextureManager hack(PhotoModeScreen screen, TextureManager textureManager) {
//        photoModeScreen = screen;
//        return textureManager;
//    }
//
//    @Override
//    public PostEffectPass addPass(String programName, Framebuffer source, Framebuffer dest, boolean linear) throws IOException {
//        AccessPostEffectProcessor access = (AccessPostEffectProcessor) this;
//
//        PhotoModePostEffectPass postEffectPass = new PhotoModePostEffectPass(photoModeScreen, access.photoMode$getResourceFactory(), programName, source, dest, linear);
//        access.photoMode$getPasses().add(access.photoMode$getPasses().size(), postEffectPass);
//        return postEffectPass;
//    }
//}
