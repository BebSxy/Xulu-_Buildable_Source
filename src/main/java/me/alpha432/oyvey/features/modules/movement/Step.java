//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.material.Material
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Step
extends Module {
    private static Step instance;
    final double[] twoFiveOffset = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
    private final double[] oneblockPositions = new double[]{0.42, 0.75};
    private final double[] twoblockPositions = new double[]{0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41, 1.57, 1.58, 1.42};
    private final double[] futurePositions = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
    private final double[] fourBlockPositions = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43, 1.78, 1.63, 1.51, 1.9, 2.21, 2.45, 2.43, 2.78, 2.63, 2.51, 2.9, 3.21, 3.45, 3.43};
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));
    public Setting<Float> stepHeightVanilla = this.register(new Setting<Object>("VHeight", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.vanilla.getValue()));
    public Setting<Integer> stepHeight = this.register(new Setting<Object>("Height", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5), v -> this.vanilla.getValue() == false));
    public Setting<Boolean> small = this.register(new Setting<Object>("Offset", Boolean.valueOf(false), v -> this.stepHeight.getValue() > 1 && this.vanilla.getValue() == false));
    public Setting<Boolean> spoof = this.register(new Setting<Object>("Spoof", Boolean.valueOf(true), v -> this.vanilla.getValue() == false));
    public Setting<Integer> ticks = this.register(new Setting<Object>("Delay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(25), v -> this.spoof.getValue() != false && this.vanilla.getValue() == false));
    public Setting<Boolean> turnOff = this.register(new Setting<Object>("Disable", Boolean.valueOf(false), v -> this.vanilla.getValue() == false));
    public Setting<Boolean> check = this.register(new Setting<Object>("Check", Boolean.valueOf(true), v -> this.vanilla.getValue() == false));
    private double[] selectedPositions = new double[0];
    private int packets;

    public Step() {
        super("Step", "Makes you step up blocks", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static Step getInstance() {
        if (instance == null) {
            instance = new Step();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        Step.mc.player.stepHeight = 0.6f;
    }

    @Override
    public void onUpdate() {
        if (this.vanilla.getValue().booleanValue()) {
            Step.mc.player.stepHeight = this.stepHeightVanilla.getValue().floatValue();
            return;
        }
        switch (this.stepHeight.getValue()) {
            case 1: {
                this.selectedPositions = this.oneblockPositions;
                break;
            }
            case 2: {
                this.selectedPositions = this.small.getValue() != false ? this.twoblockPositions : this.futurePositions;
                break;
            }
            case 3: {
                this.selectedPositions = this.twoFiveOffset;
            }
            case 4: {
                this.selectedPositions = this.fourBlockPositions;
            }
        }
        if (Step.mc.player.collidedHorizontally && Step.mc.player.onGround) {
            ++this.packets;
        }
        AxisAlignedBB bb = Step.mc.player.getEntityBoundingBox();
        if (this.check.getValue().booleanValue()) {
            for (int x = MathHelper.floor((double)bb.minX); x < MathHelper.floor((double)(bb.maxX + 1.0)); ++x) {
                for (int z = MathHelper.floor((double)bb.minZ); z < MathHelper.floor((double)(bb.maxZ + 1.0)); ++z) {
                    Block block = Step.mc.world.getBlockState(new BlockPos((double)x, bb.maxY + 1.0, (double)z)).getBlock();
                    if (block instanceof BlockAir) continue;
                    return;
                }
            }
        }
        if (Step.mc.player.onGround && !Step.mc.player.isInsideOfMaterial(Material.WATER) && !Step.mc.player.isInsideOfMaterial(Material.LAVA) && Step.mc.player.collidedVertically && Step.mc.player.fallDistance == 0.0f && !Step.mc.gameSettings.keyBindJump.pressed && Step.mc.player.collidedHorizontally && !Step.mc.player.isOnLadder() && (this.packets > this.selectedPositions.length - 2 || this.spoof.getValue().booleanValue() && this.packets > this.ticks.getValue())) {
            for (double position : this.selectedPositions) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + position, Step.mc.player.posZ, true));
            }
            Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + this.selectedPositions[this.selectedPositions.length - 1], Step.mc.player.posZ);
            this.packets = 0;
            if (this.turnOff.getValue().booleanValue()) {
                this.disable();
            }
        }
    }
}

