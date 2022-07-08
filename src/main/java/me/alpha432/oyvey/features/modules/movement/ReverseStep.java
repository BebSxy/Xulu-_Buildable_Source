//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class ReverseStep
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 0, 0, 20));

    public ReverseStep() {
        super("ReverseStep", "Speeds up downwards motion", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (ReverseStep.mc.player.onGround) {
            ReverseStep.mc.player.motionY -= (double)this.speed.getValue().intValue();
        }
    }
}

