//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 */
package me.alpha432.oyvey.util.shader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class EfficientTexture
extends AbstractTexture {
    private int[] textureData;
    private final int width;
    private final int height;

    public EfficientTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.textureData, 0, bufferedImage.getWidth());
        this.updateEfficientTexture();
    }

    public EfficientTexture(int textureWidth, int textureHeight) {
        this.width = textureWidth;
        this.height = textureHeight;
        this.textureData = new int[textureWidth * textureHeight];
        TextureUtil.allocateTexture((int)this.getGlTextureId(), (int)textureWidth, (int)textureHeight);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
    }

    private void updateEfficientTexture() {
        TextureUtil.uploadTexture((int)this.getGlTextureId(), (int[])this.textureData, (int)this.width, (int)this.height);
        this.textureData = new int[0];
    }
}

