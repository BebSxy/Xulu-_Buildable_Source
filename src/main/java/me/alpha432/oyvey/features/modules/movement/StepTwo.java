//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.alpha432.oyvey.features.modules.movement;

import java.text.DecimalFormat;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.Speed;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class StepTwo
extends Module {
    private static StepTwo instance;
    Setting<Double> height = this.register(new Setting<Double>("height", 2.5, 0.5, 2.5));
    Setting<Mode> mode = this.register(new Setting<Mode>("mode", Mode.Vanilla));
    private int ticks = 0;

    public StepTwo() {
        super("Step[Rewr]", "s", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static StepTwo getInstance() {
        if (instance == null) {
            instance = new StepTwo();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        StepTwo.mc.player.stepHeight = 0.6f;
    }

    @Override
    public void onUpdate() {
        if (StepTwo.mc.world == null || StepTwo.mc.player == null) {
            return;
        }
        if (StepTwo.mc.player.isInWater() || StepTwo.mc.player.isInLava() || StepTwo.mc.player.isOnLadder() || StepTwo.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (OyVey.moduleManager.getModuleByClass(Speed.class).isEnabled()) {
            return;
        }
        if (this.mode.getValue() == Mode.Normal) {
            double[] dir = StepTwo.forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 2.6, dir[1])).isEmpty() && !StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 2.1, dir[1])).isEmpty() && !StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 1.6, dir[1])).isEmpty() && !StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 1.0, dir[1])).isEmpty() && !StepTwo.mc.world.getCollisionBoxes((Entity)StepTwo.mc.player, StepTwo.mc.player.getEntityBoundingBox().offset(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (StepTwo.mc.player.collidedHorizontally && (StepTwo.mc.player.moveForward != 0.0f || StepTwo.mc.player.moveStrafing != 0.0f) && StepTwo.mc.player.onGround) {
                int i;
                if (one && this.height.getValue() >= 1.0) {
                    double[] oneOffset = new double[]{0.42, 0.753};
                    for (i = 0; i < oneOffset.length; ++i) {
                        StepTwo.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(StepTwo.mc.player.posX, StepTwo.mc.player.posY + oneOffset[i], StepTwo.mc.player.posZ, StepTwo.mc.player.onGround));
                    }
                    StepTwo.mc.player.setPosition(StepTwo.mc.player.posX, StepTwo.mc.player.posY + 1.0, StepTwo.mc.player.posZ);
                    this.ticks = 1;
                }
                if (onefive && this.height.getValue() >= 1.5) {
                    double[] oneFiveOffset = new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
                    for (i = 0; i < oneFiveOffset.length; ++i) {
                        StepTwo.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(StepTwo.mc.player.posX, StepTwo.mc.player.posY + oneFiveOffset[i], StepTwo.mc.player.posZ, StepTwo.mc.player.onGround));
                    }
                    StepTwo.mc.player.setPosition(StepTwo.mc.player.posX, StepTwo.mc.player.posY + 1.5, StepTwo.mc.player.posZ);
                    this.ticks = 1;
                }
                if (two && this.height.getValue() >= 2.0) {
                    double[] twoOffset = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
                    for (i = 0; i < twoOffset.length; ++i) {
                        StepTwo.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(StepTwo.mc.player.posX, StepTwo.mc.player.posY + twoOffset[i], StepTwo.mc.player.posZ, StepTwo.mc.player.onGround));
                    }
                    StepTwo.mc.player.setPosition(StepTwo.mc.player.posX, StepTwo.mc.player.posY + 2.0, StepTwo.mc.player.posZ);
                    this.ticks = 2;
                }
                if (twofive && this.height.getValue() >= 2.5) {
                    double[] twoFiveOffset = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                    for (i = 0; i < twoFiveOffset.length; ++i) {
                        StepTwo.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(StepTwo.mc.player.posX, StepTwo.mc.player.posY + twoFiveOffset[i], StepTwo.mc.player.posZ, StepTwo.mc.player.onGround));
                    }
                    StepTwo.mc.player.setPosition(StepTwo.mc.player.posX, StepTwo.mc.player.posY + 2.5, StepTwo.mc.player.posZ);
                    this.ticks = 2;
                }
            }
        }
        if (this.mode.getValue() == Mode.Vanilla) {
            DecimalFormat df = new DecimalFormat("#");
            StepTwo.mc.player.stepHeight = Float.parseFloat(df.format(this.height.getValue()));
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onDisable() {
        StepTwo.mc.player.stepHeight = 0.5f;
    }

    public static double[] forward(double speed) {
        float forward = StepTwo.mc.player.movementInput.moveForward;
        float side = StepTwo.mc.player.movementInput.moveStrafe;
        float yaw = StepTwo.mc.player.prevRotationYaw + (StepTwo.mc.player.rotationYaw - StepTwo.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static enum Mode {
        Vanilla,
        Normal;

    }
}

