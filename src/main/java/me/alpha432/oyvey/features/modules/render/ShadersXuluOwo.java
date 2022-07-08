//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ShadersXuluOwo
extends Module {
    private static final ShadersXuluOwo INSTANCE = new ShadersXuluOwo();
    public Setting<Mode> shader = this.register(new Setting<Mode>("Mode", Mode.green));

    public ShadersXuluOwo() {
        super("Shaders", "Cool.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (ShadersXuluOwo.mc.entityRenderer.getShaderGroup() != null) {
                ShadersXuluOwo.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                ShadersXuluOwo.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + (Object)((Object)this.shader.getValue()) + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ShadersXuluOwo.mc.entityRenderer.getShaderGroup() != null && ShadersXuluOwo.mc.currentScreen == null) {
            ShadersXuluOwo.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.shader.currentEnumName();
    }

    @Override
    public void onDisable() {
        if (ShadersXuluOwo.mc.entityRenderer.getShaderGroup() != null) {
            ShadersXuluOwo.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public static enum Mode {
        notch,
        antialias,
        art,
        bits,
        blobs,
        blobs2,
        blur,
        bumpy,
        color_convolve,
        creeper,
        deconverge,
        desaturate,
        flip,
        fxaa,
        green,
        invert,
        ntsc,
        pencil,
        phosphor,
        sobel,
        spider,
        wobble;

    }
}

