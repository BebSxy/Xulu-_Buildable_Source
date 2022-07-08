//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.entity.Entity;

public class TickShift
extends Module {
    Setting<Integer> ticksVal = this.register(new Setting<Integer>("Ticks", 18, 1, 100));
    Setting<Float> timer = this.register(new Setting<Float>("Timer", Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(3.0f)));
    boolean canTimer = false;
    int tick = 0;

    public TickShift() {
        super("TickShift", "Makes you go Faster", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.canTimer = false;
        this.tick = 0;
    }

    @Override
    public void onUpdate() {
        if (this.tick <= 0) {
            this.tick = 0;
            this.canTimer = false;
            TickShift.mc.timer.tickLength = 50.0f;
        }
        if (this.tick > 0 && EntityUtil.isEntityMoving((Entity)TickShift.mc.player)) {
            --this.tick;
            TickShift.mc.timer.tickLength = 50.0f / this.timer.getValue().floatValue();
        }
        if (!EntityUtil.isEntityMoving((Entity)TickShift.mc.player)) {
            ++this.tick;
        }
        if (this.tick >= this.ticksVal.getValue()) {
            this.tick = this.ticksVal.getValue();
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(this.tick);
    }

    @Override
    public void onDisable() {
        TickShift.mc.timer.tickLength = 50.0f;
    }
}

