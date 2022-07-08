//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemElytra
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import java.util.Objects;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ElytraFlightPhob
extends Module {
    private static ElytraFlightPhob INSTANCE = new ElytraFlightPhob();
    private final Timer timer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Modes", Mode.FLY));
    public Setting<Integer> devMode = this.register(new Setting<Object>("Type", 2, 1, 3, v -> this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER, "EventMode"));
    public Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE && this.mode.getValue() != Mode.LOOK, "The Speed."));
    public Setting<Float> vSpeed = this.register(new Setting<Object>("VSpeed", Float.valueOf(0.3f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE || this.mode.getValue() == Mode.LOOK, "Vertical Speed"));
    public Setting<Float> hSpeed = this.register(new Setting<Object>("HSpeed", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE || this.mode.getValue() == Mode.LOOK, "Horizontal Speed"));
    public Setting<Float> glide = this.register(new Setting<Object>("Gliding", Float.valueOf(1.0E-4f), Float.valueOf(0.0f), Float.valueOf(0.2f), v -> this.mode.getValue() == Mode.BETTER, "Glide Speed"));
    public Setting<Float> tooBeeSpeed = this.register(new Setting<Object>("StrictSpeed", Float.valueOf(1.8000001f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.mode.getValue() == Mode.TOOBEE, "Speed for flight on 2b2t"));
    public Setting<Boolean> autoStart = this.register(new Setting<Boolean>("StartOffGround", true));
    public Setting<Boolean> disableInLiquid = this.register(new Setting<Boolean>("WaterDisable", true));
    public Setting<Boolean> infiniteDura = this.register(new Setting<Boolean>("InfiniteDurability", false));
    public Setting<Boolean> noKick = this.register(new Setting<Object>("ForceStay", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Boolean> allowUp = this.register(new Setting<Object>("AllowUp", Boolean.TRUE, v -> this.mode.getValue() == Mode.BETTER));
    public Setting<Boolean> lockPitch = this.register(new Setting<Boolean>("LockPitch", false));
    private Double posX;
    private Double flyHeight;
    private Double posZ;

    public ElytraFlightPhob() {
        super("ElytraFlight", "Exactly this", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ElytraFlightPhob getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ElytraFlightPhob();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.BETTER && !this.autoStart.getValue().booleanValue() && this.devMode.getValue() == 1) {
            ElytraFlightPhob.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.flyHeight = null;
        this.posX = null;
        this.posZ = null;
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 1 && ElytraFlightPhob.mc.player.isElytraFlying()) {
            ElytraFlightPhob.mc.player.motionX = 0.0;
            ElytraFlightPhob.mc.player.motionY = -1.0E-4;
            ElytraFlightPhob.mc.player.motionZ = 0.0;
            double forwardInput = ElytraFlightPhob.mc.player.movementInput.moveForward;
            double strafeInput = ElytraFlightPhob.mc.player.movementInput.moveStrafe;
            double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlightPhob.mc.player.rotationYaw);
            double forward = result[0];
            double strafe = result[1];
            double yaw = result[2];
            if (forwardInput != 0.0 || strafeInput != 0.0) {
                double cos = Math.cos(Math.toRadians(yaw + 90.0));
                double sin = Math.sin(Math.toRadians(yaw + 90.0));
                ElytraFlightPhob.mc.player.motionX = forward * (double)this.speed.getValue().floatValue() * cos + strafe * (double)this.speed.getValue().floatValue() * sin;
                ElytraFlightPhob.mc.player.motionZ = forward * (double)this.speed.getValue().floatValue() * sin - strafe * (double)this.speed.getValue().floatValue() * cos;
            }
            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ElytraFlightPhob.mc.player.motionY = -1.0;
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
            event.getPacket();
            ElytraFlightPhob.mc.player.isElytraFlying();
        }
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEEBYPASS) {
            event.getPacket();
            ElytraFlightPhob.mc.player.isElytraFlying();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.mode.getValue() == Mode.LOOK && ElytraFlightPhob.mc.player.isElytraFlying()) {
            ElytraFlightPhob.mc.player.motionY = 0.0;
            ElytraFlightPhob.mc.player.motionX = 0.0;
            ElytraFlightPhob.mc.player.motionZ = 0.0;
            if (ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                ElytraFlightPhob.mc.player.motionY = (double)this.hSpeed.getValue().floatValue() * -((double)ElytraFlightPhob.mc.player.movementInput.moveForward * Math.sin(MathUtil.degToRad(ElytraFlightPhob.mc.player.rotationPitch)));
            }
            if (ElytraFlightPhob.mc.player.movementInput.jump) {
                ElytraFlightPhob.mc.player.motionY = this.vSpeed.getValue().floatValue();
            } else if (ElytraFlightPhob.mc.player.movementInput.sneak) {
                ElytraFlightPhob.mc.player.motionY = -1.0f * this.vSpeed.getValue().floatValue();
            }
            double forward = ElytraFlightPhob.mc.player.movementInput.moveForward;
            double strafe = ElytraFlightPhob.mc.player.movementInput.moveStrafe;
            float yaw = ElytraFlightPhob.mc.player.rotationYaw;
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f && ElytraFlightPhob.mc.player.movementInput.moveForward == 0.0f) {
                ElytraFlightPhob.mc.player.motionX = forward * (double)this.hSpeed.getValue().floatValue() * cos + strafe * (double)this.hSpeed.getValue().floatValue() * sin;
                ElytraFlightPhob.mc.player.motionZ = forward * (double)this.hSpeed.getValue().floatValue() * sin - strafe * (double)this.hSpeed.getValue().floatValue() * cos;
            } else if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                ElytraFlightPhob.mc.player.motionX = (forward * (double)this.hSpeed.getValue().floatValue() * cos + strafe * (double)this.hSpeed.getValue().floatValue() * sin) * Math.cos(Math.abs(MathUtil.degToRad(ElytraFlightPhob.mc.player.rotationPitch)));
                ElytraFlightPhob.mc.player.motionZ = (forward * (double)this.hSpeed.getValue().floatValue() * sin - strafe * (double)this.hSpeed.getValue().floatValue() * cos) * Math.cos(Math.abs(MathUtil.degToRad(ElytraFlightPhob.mc.player.rotationPitch)));
            }
            event.setX(ElytraFlightPhob.mc.player.motionX);
            event.setY(ElytraFlightPhob.mc.player.motionY);
            event.setZ(ElytraFlightPhob.mc.player.motionZ);
        }
        if (this.mode.getValue() == Mode.OHARE) {
            ItemStack itemstack = ElytraFlightPhob.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable((ItemStack)itemstack) && ElytraFlightPhob.mc.player.isElytraFlying()) {
                event.setY(ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown() ? (double)this.vSpeed.getValue().floatValue() : (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown() ? (double)(-this.vSpeed.getValue().floatValue()) : 0.0));
                ElytraFlightPhob.mc.player.addVelocity(0.0, ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown() ? (double)this.vSpeed.getValue().floatValue() : (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown() ? (double)(-this.vSpeed.getValue().floatValue()) : 0.0), 0.0);
                ElytraFlightPhob.mc.player.rotateElytraX = 0.0f;
                ElytraFlightPhob.mc.player.rotateElytraY = 0.0f;
                ElytraFlightPhob.mc.player.rotateElytraZ = 0.0f;
                ElytraFlightPhob.mc.player.moveVertical = ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown() ? this.vSpeed.getValue().floatValue() : (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown() ? -this.vSpeed.getValue().floatValue() : 0.0f);
                double forward = ElytraFlightPhob.mc.player.movementInput.moveForward;
                double strafe = ElytraFlightPhob.mc.player.movementInput.moveStrafe;
                float yaw = ElytraFlightPhob.mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += (float)(forward > 0.0 ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += (float)(forward > 0.0 ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * (double)this.hSpeed.getValue().floatValue() * cos + strafe * (double)this.hSpeed.getValue().floatValue() * sin);
                    event.setZ(forward * (double)this.hSpeed.getValue().floatValue() * sin - strafe * (double)this.hSpeed.getValue().floatValue() * cos);
                }
            }
        } else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 3) {
            if (ElytraFlightPhob.mc.player.isElytraFlying()) {
                event.setX(0.0);
                event.setY(-1.0E-4);
                event.setZ(0.0);
                double forwardInput = ElytraFlightPhob.mc.player.movementInput.moveForward;
                double strafeInput = ElytraFlightPhob.mc.player.movementInput.moveStrafe;
                double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFlightPhob.mc.player.rotationYaw);
                double forward = result[0];
                double strafe = result[1];
                double yaw = result[2];
                if (forwardInput != 0.0 || strafeInput != 0.0) {
                    double cos = Math.cos(Math.toRadians(yaw + 90.0));
                    double sin = Math.sin(Math.toRadians(yaw + 90.0));
                    event.setX(forward * (double)this.speed.getValue().floatValue() * cos + strafe * (double)this.speed.getValue().floatValue() * sin);
                    event.setY(forward * (double)this.speed.getValue().floatValue() * sin - strafe * (double)this.speed.getValue().floatValue() * cos);
                }
                if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(-1.0);
                }
            }
        } else if (this.mode.getValue() == Mode.TOOBEE) {
            if (!ElytraFlightPhob.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFlightPhob.mc.player.movementInput.jump) {
                if (ElytraFlightPhob.mc.player.movementInput.sneak) {
                    ElytraFlightPhob.mc.player.motionY = -(this.tooBeeSpeed.getValue().floatValue() / 2.0f);
                    event.setY(-(this.speed.getValue().floatValue() / 2.0f));
                } else if (event.getY() != -1.01E-4) {
                    event.setY(-1.01E-4);
                    ElytraFlightPhob.mc.player.motionY = -1.01E-4;
                }
            } else {
                return;
            }
            this.setMoveSpeed(event, this.tooBeeSpeed.getValue().floatValue());
        } else if (this.mode.getValue() == Mode.TOOBEEBYPASS) {
            if (!ElytraFlightPhob.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFlightPhob.mc.player.movementInput.jump) {
                if (this.lockPitch.getValue().booleanValue()) {
                    ElytraFlightPhob.mc.player.rotationPitch = 4.0f;
                }
            } else {
                return;
            }
            if (OyVey.speedManager.getSpeedKpH() > 180.0) {
                return;
            }
            double yaw = Math.toRadians(ElytraFlightPhob.mc.player.rotationYaw);
            ElytraFlightPhob.mc.player.motionX -= (double)ElytraFlightPhob.mc.player.movementInput.moveForward * Math.sin(yaw) * 0.04;
            ElytraFlightPhob.mc.player.motionZ += (double)ElytraFlightPhob.mc.player.movementInput.moveForward * Math.cos(yaw) * 0.04;
        }
    }

    private void setMoveSpeed(MoveEvent event, double speed) {
        double forward = ElytraFlightPhob.mc.player.movementInput.moveForward;
        double strafe = ElytraFlightPhob.mc.player.movementInput.moveStrafe;
        float yaw = ElytraFlightPhob.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
            ElytraFlightPhob.mc.player.motionX = 0.0;
            ElytraFlightPhob.mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            event.setX(x);
            event.setZ(z);
            ElytraFlightPhob.mc.player.motionX = x;
            ElytraFlightPhob.mc.player.motionZ = z;
        }
    }

    @Override
    public void onTick() {
        if (!ElytraFlightPhob.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFlightPhob.mc.player.isInWater()) {
                    Objects.requireNonNull(mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                    ElytraFlightPhob.mc.player.motionY += 0.08;
                } else if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    ElytraFlightPhob.mc.player.motionY -= 0.04;
                }
                if (ElytraFlightPhob.mc.gameSettings.keyBindForward.isKeyDown()) {
                    float yaw = (float)Math.toRadians(ElytraFlightPhob.mc.player.rotationYaw);
                    ElytraFlightPhob.mc.player.motionX -= (double)(MathHelper.sin((float)yaw) * 0.05f);
                    ElytraFlightPhob.mc.player.motionZ += (double)(MathHelper.cos((float)yaw) * 0.05f);
                    break;
                }
                if (!ElytraFlightPhob.mc.gameSettings.keyBindBack.isKeyDown()) break;
                float yaw = (float)Math.toRadians(ElytraFlightPhob.mc.player.rotationYaw);
                ElytraFlightPhob.mc.player.motionX += (double)(MathHelper.sin((float)yaw) * 0.05f);
                ElytraFlightPhob.mc.player.motionZ -= (double)(MathHelper.cos((float)yaw) * 0.05f);
                break;
            }
            case FLY: {
                ElytraFlightPhob.mc.player.capabilities.isFlying = true;
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (ElytraFlightPhob.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                if (this.disableInLiquid.getValue().booleanValue() && (ElytraFlightPhob.mc.player.isInWater() || ElytraFlightPhob.mc.player.isInLava())) {
                    if (ElytraFlightPhob.mc.player.isElytraFlying()) {
                        Objects.requireNonNull(mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    return;
                }
                if (this.autoStart.getValue().booleanValue() && ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown() && !ElytraFlightPhob.mc.player.isElytraFlying() && ElytraFlightPhob.mc.player.motionY < 0.0 && this.timer.passedMs(250L)) {
                    Objects.requireNonNull(mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.timer.reset();
                }
                if (this.mode.getValue() == Mode.BETTER) {
                    double[] dir = MathUtil.directionSpeed(this.devMode.getValue() == 1 ? (double)this.speed.getValue().floatValue() : (double)this.hSpeed.getValue().floatValue());
                    switch (this.devMode.getValue()) {
                        case 1: {
                            ElytraFlightPhob.mc.player.setVelocity(0.0, 0.0, 0.0);
                            ElytraFlightPhob.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
                            if (ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY += (double)this.speed.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY -= (double)this.speed.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlightPhob.mc.player.motionX = dir[0];
                                ElytraFlightPhob.mc.player.motionZ = dir[1];
                                break;
                            }
                            ElytraFlightPhob.mc.player.motionX = 0.0;
                            ElytraFlightPhob.mc.player.motionZ = 0.0;
                            break;
                        }
                        case 2: {
                            if (ElytraFlightPhob.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null) {
                                    this.flyHeight = ElytraFlightPhob.mc.player.posY;
                                }
                            } else {
                                this.flyHeight = null;
                                return;
                            }
                            if (this.noKick.getValue().booleanValue()) {
                                this.flyHeight = this.flyHeight - (double)this.glide.getValue().floatValue();
                            }
                            this.posX = 0.0;
                            this.posZ = 0.0;
                            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = dir[0];
                                this.posZ = dir[1];
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFlightPhob.mc.player.posY + (double)this.vSpeed.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFlightPhob.mc.player.posY - (double)this.vSpeed.getValue().floatValue();
                            }
                            ElytraFlightPhob.mc.player.setPosition(ElytraFlightPhob.mc.player.posX + this.posX, this.flyHeight.doubleValue(), ElytraFlightPhob.mc.player.posZ + this.posZ);
                            ElytraFlightPhob.mc.player.setVelocity(0.0, 0.0, 0.0);
                            break;
                        }
                        case 3: {
                            if (ElytraFlightPhob.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null || this.posX == null || this.posX == 0.0 || this.posZ == null || this.posZ == 0.0) {
                                    this.flyHeight = ElytraFlightPhob.mc.player.posY;
                                    this.posX = ElytraFlightPhob.mc.player.posX;
                                    this.posZ = ElytraFlightPhob.mc.player.posZ;
                                }
                            } else {
                                this.flyHeight = null;
                                this.posX = null;
                                this.posZ = null;
                                return;
                            }
                            if (this.noKick.getValue().booleanValue()) {
                                this.flyHeight = this.flyHeight - (double)this.glide.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = this.posX + dir[0];
                                this.posZ = this.posZ + dir[1];
                            }
                            if (this.allowUp.getValue().booleanValue() && ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFlightPhob.mc.player.posY + (double)(this.vSpeed.getValue().floatValue() / 10.0f);
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFlightPhob.mc.player.posY - (double)(this.vSpeed.getValue().floatValue() / 10.0f);
                            }
                            ElytraFlightPhob.mc.player.setPosition(this.posX.doubleValue(), this.flyHeight.doubleValue(), this.posZ.doubleValue());
                            ElytraFlightPhob.mc.player.setVelocity(0.0, 0.0, 0.0);
                        }
                    }
                }
                double rotationYaw = Math.toRadians(ElytraFlightPhob.mc.player.rotationYaw);
                if (ElytraFlightPhob.mc.player.isElytraFlying()) {
                    switch (this.mode.getValue()) {
                        case VANILLA: {
                            float speedScaled = this.speed.getValue().floatValue() * 0.05f;
                            if (ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY += (double)speedScaled;
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY -= (double)speedScaled;
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindForward.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionX -= Math.sin(rotationYaw) * (double)speedScaled;
                                ElytraFlightPhob.mc.player.motionZ += Math.cos(rotationYaw) * (double)speedScaled;
                            }
                            if (!ElytraFlightPhob.mc.gameSettings.keyBindBack.isKeyDown()) break;
                            ElytraFlightPhob.mc.player.motionX += Math.sin(rotationYaw) * (double)speedScaled;
                            ElytraFlightPhob.mc.player.motionZ -= Math.cos(rotationYaw) * (double)speedScaled;
                            break;
                        }
                        case PACKET: {
                            this.freezePlayer((EntityPlayer)ElytraFlightPhob.mc.player);
                            this.runNoKick((EntityPlayer)ElytraFlightPhob.mc.player);
                            double[] directionSpeedPacket = MathUtil.directionSpeed(this.speed.getValue().floatValue());
                            if (ElytraFlightPhob.mc.player.movementInput.jump) {
                                ElytraFlightPhob.mc.player.motionY = this.speed.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.player.movementInput.sneak) {
                                ElytraFlightPhob.mc.player.motionY = -this.speed.getValue().floatValue();
                            }
                            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlightPhob.mc.player.motionX = directionSpeedPacket[0];
                                ElytraFlightPhob.mc.player.motionZ = directionSpeedPacket[1];
                            }
                            Objects.requireNonNull(mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            break;
                        }
                        case BYPASS: {
                            if (this.devMode.getValue() != 3) break;
                            if (ElytraFlightPhob.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY = 0.02f;
                            }
                            if (ElytraFlightPhob.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFlightPhob.mc.player.motionY = -0.2f;
                            }
                            if (ElytraFlightPhob.mc.player.ticksExisted % 8 == 0 && ElytraFlightPhob.mc.player.posY <= 240.0) {
                                ElytraFlightPhob.mc.player.motionY = 0.02f;
                            }
                            ElytraFlightPhob.mc.player.capabilities.isFlying = true;
                            ElytraFlightPhob.mc.player.capabilities.setFlySpeed(0.025f);
                            double[] directionSpeedBypass = MathUtil.directionSpeed(0.52f);
                            if (ElytraFlightPhob.mc.player.movementInput.moveStrafe != 0.0f || ElytraFlightPhob.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFlightPhob.mc.player.motionX = directionSpeedBypass[0];
                                ElytraFlightPhob.mc.player.motionZ = directionSpeedBypass[1];
                                break;
                            }
                            ElytraFlightPhob.mc.player.motionX = 0.0;
                            ElytraFlightPhob.mc.player.motionZ = 0.0;
                        }
                    }
                }
                if (!this.infiniteDura.getValue().booleanValue()) break;
                ElytraFlightPhob.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                break;
            }
            case 1: {
                if (!this.infiniteDura.getValue().booleanValue()) break;
                ElytraFlightPhob.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlightPhob.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    private double[] forwardStrafeYaw(double forward, double strafe, double yaw) {
        double[] result = new double[]{forward, strafe, yaw};
        if ((forward != 0.0 || strafe != 0.0) && forward != 0.0) {
            if (strafe > 0.0) {
                result[2] = result[2] + (double)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                result[2] = result[2] + (double)(forward > 0.0 ? 45 : -45);
            }
            result[1] = 0.0;
            if (forward > 0.0) {
                result[0] = 1.0;
            } else if (forward < 0.0) {
                result[0] = -1.0;
            }
        }
        return result;
    }

    private void freezePlayer(EntityPlayer player) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
    }

    private void runNoKick(EntityPlayer player) {
        if (this.noKick.getValue().booleanValue() && !player.isElytraFlying() && player.ticksExisted % 4 == 0) {
            player.motionY = -0.04f;
        }
    }

    @Override
    public void onDisable() {
        if (ElytraFlightPhob.fullNullCheck() || ElytraFlightPhob.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFlightPhob.mc.player.capabilities.isFlying = false;
    }

    public static enum Mode {
        VANILLA,
        PACKET,
        BOOST,
        FLY,
        BYPASS,
        BETTER,
        OHARE,
        TOOBEE,
        TOOBEEBYPASS,
        LOOK;

    }
}

