//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL20
 */
package me.alpha432.oyvey.util.shader.shaders;

import me.alpha432.oyvey.util.shader.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class ItemShader
extends FramebufferShader {
    private static ItemShader INSTANCE;
    protected float time = 0.0f;

    private ItemShader() {
        super("item.frag");
    }

    public static ItemShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("dimensions");
        this.setupUniform("texture");
        this.setupUniform("image");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
        this.setupUniform("blur");
        this.setupUniform("minAlpha");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        GL20.glUniform2f((int)this.getUniform("dimensions"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform1i((int)this.getUniform("texture"), (int)0);
        GL20.glUniform1i((int)this.getUniform("image"), (int)0);
        GL20.glUniform3f((int)this.getUniform("color"), (float)this.red, (float)this.green, (float)this.blue);
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform1f((int)this.getUniform("divider"), (float)this.divider);
        GL20.glUniform1f((int)this.getUniform("maxSample"), (float)this.maxSample);
        GL20.glUniform1i((int)this.getUniform("blur"), (int)1);
        GL20.glUniform1f((int)this.getUniform("minAlpha"), (float)1.0f);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.001 * (double)this.animationSpeed);
    }
}

