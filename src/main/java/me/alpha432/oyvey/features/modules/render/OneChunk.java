//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;

public class OneChunk
extends Module {
    private static OneChunk INSTANCE;
    private int renderDistance;

    private OneChunk() {
        super("OneChunk", "Sets your render distance to 1", Module.Category.RENDER, false, false, true);
        this.renderDistance = OneChunk.mc.gameSettings.renderDistanceChunks;
    }

    public static OneChunk getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OneChunk();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.renderDistance = OneChunk.mc.gameSettings.renderDistanceChunks;
        OneChunk.mc.gameSettings.renderDistanceChunks = 1;
    }

    @Override
    public void onDisable() {
        OneChunk.mc.gameSettings.renderDistanceChunks = this.renderDistance;
    }
}

