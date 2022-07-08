//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SprintFuture
extends Module {
    private static SprintFuture INSTANCE = new SprintFuture();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.LEGIT));

    public SprintFuture() {
        super("SprintFuture", "Modifies sprinting", Module.Category.MOVEMENT, false, false, false);
        this.setInstance();
    }

    public static SprintFuture getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SprintFuture();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (SprintFuture.mc.player.movementInput.moveForward != 0.0f || SprintFuture.mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if (!SprintFuture.mc.gameSettings.keyBindForward.isKeyDown() && !SprintFuture.mc.gameSettings.keyBindBack.isKeyDown() && !SprintFuture.mc.gameSettings.keyBindLeft.isKeyDown() && !SprintFuture.mc.gameSettings.keyBindRight.isKeyDown() || SprintFuture.mc.player.isSneaking() || SprintFuture.mc.player.collidedHorizontally || (float)SprintFuture.mc.player.getFoodStats().getFoodLevel() <= 6.0f) break;
                SprintFuture.mc.player.setSprinting(true);
                break;
            }
            case LEGIT: {
                if (!SprintFuture.mc.gameSettings.keyBindForward.isKeyDown() || SprintFuture.mc.player.isSneaking() || SprintFuture.mc.player.isHandActive() || SprintFuture.mc.player.collidedHorizontally || (float)SprintFuture.mc.player.getFoodStats().getFoodLevel() <= 6.0f || SprintFuture.mc.currentScreen != null) break;
                SprintFuture.mc.player.setSprinting(true);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!SprintFuture.nullCheck()) {
            SprintFuture.mc.player.setSprinting(false);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        LEGIT,
        RAGE;

    }
}

