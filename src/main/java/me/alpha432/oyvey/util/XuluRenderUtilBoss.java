//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityAgeable
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.passive.EntityAmbientCreature
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityFireball
 *  net.minecraft.entity.projectile.EntityShulkerBullet
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.util;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.mixin.mixins.XuluProtect.XuluLivingBase;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class XuluRenderUtilBoss
implements Util {
    public static final Vec3d[] antiDropOffsetList = new Vec3d[]{new Vec3d(0.0, -2.0, 0.0)};
    public static final Vec3d[] platformOffsetList = new Vec3d[]{new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0)};
    public static final Vec3d[] legOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)};
    public static final Vec3d[] doubleLegOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0)};
    public static final Vec3d[] OffsetList = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] headpiece = new Vec3d[]{new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] offsetsNoHead = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0)};
    public static final Vec3d[] antiStepOffsetList = new Vec3d[]{new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0)};
    public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[]{new Vec3d(0.0, 3.0, 0.0)};

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            XuluRenderUtilBoss.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        } else {
            XuluRenderUtilBoss.mc.playerController.attackEntity((EntityPlayer)XuluRenderUtilBoss.mc.player, entity);
        }
        if (swingArm) {
            XuluRenderUtilBoss.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)time);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(XuluRenderUtilBoss.getInterpolatedAmount(entity, partialTicks));
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
        return XuluRenderUtilBoss.getInterpolatedPos(entity, partialTicks).subtract(XuluRenderUtilBoss.mc.getRenderManager().renderPosX, XuluRenderUtilBoss.mc.getRenderManager().renderPosY, XuluRenderUtilBoss.mc.getRenderManager().renderPosZ);
    }

    public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z).subtract(XuluRenderUtilBoss.mc.getRenderManager().renderPosX, XuluRenderUtilBoss.mc.getRenderManager().renderPosY, XuluRenderUtilBoss.mc.getRenderManager().renderPosZ);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return XuluRenderUtilBoss.getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return XuluRenderUtilBoss.getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf)entity).isAngry()) {
            return false;
        }
        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid) {
            return true;
        }
        return entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null;
    }

    public static boolean isSafe(Entity entity, int height, boolean floor, boolean face) {
        return XuluRenderUtilBoss.getUnsafeBlocks(entity, height, floor, face).size() == 0;
    }

    public static boolean stopSneaking(boolean isSneaking) {
        if (isSneaking && XuluRenderUtilBoss.mc.player != null) {
            XuluRenderUtilBoss.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)XuluRenderUtilBoss.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }

    public static boolean isSafe(Entity entity) {
        return XuluRenderUtilBoss.isSafe(entity, 0, false, true);
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor, boolean face) {
        return XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(entity.getPositionVector(), height, floor, face);
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                return true;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).isAngry() && !XuluRenderUtilBoss.mc.player.equals((Object)((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).isScreaming();
            }
        }
        return XuluRenderUtilBoss.isHostileMob(entity);
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isProjectile(Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }

    public static boolean isVehicle(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isFriendlyMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !XuluRenderUtilBoss.isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || XuluRenderUtilBoss.isNeutralMob(entity) && !XuluRenderUtilBoss.isMobAggressive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !XuluRenderUtilBoss.isNeutralMob(entity);
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (Vec3d vector : XuluRenderUtilBoss.getOffsets(height, floor, face)) {
            BlockPos targetPos = new BlockPos(pos).add(vector.x, vector.y, vector.z);
            Block block = XuluRenderUtilBoss.mc.world.getBlockState(targetPos).getBlock();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInHole(Entity entity) {
        return XuluRenderUtilBoss.isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return XuluRenderUtilBoss.isBedrockHole(blockPos) || XuluRenderUtilBoss.isObbyHole(blockPos) || XuluRenderUtilBoss.isBothHole(blockPos);
    }

    public static boolean isCrystalAtFeet(EntityEnderCrystal crystal, double range) {
        for (EntityPlayer player : XuluRenderUtilBoss.mc.world.playerEntities) {
            if (XuluRenderUtilBoss.mc.player.getDistanceSq((Entity)player) > range * range || OyVey.friendManager.isFriend(player)) continue;
            for (Vec3d vec : doubleLegOffsetList) {
                if (new BlockPos(player.getPositionVector()).add(vec.x, vec.y, vec.z) != crystal.getPosition()) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN)) continue;
            return false;
        }
        return true;
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor, boolean face) {
        List<Vec3d> list = XuluRenderUtilBoss.getUnsafeBlocks(entity, height, floor, face);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
        List<Vec3d> list = XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(pos, height, floor, face);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static double getDst(Vec3d vec) {
        return XuluRenderUtilBoss.mc.player.getPositionVector().distanceTo(vec);
    }

    public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        return XuluRenderUtilBoss.getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop, face).size() == 0;
    }

    public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
        return XuluRenderUtilBoss.getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace, noScaffoldExtend, face).size() == 0;
    }

    public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && XuluRenderUtilBoss.getUnsafeBlocks((Entity)player, 2, false, face).size() == 4) {
            vec3ds.addAll(XuluRenderUtilBoss.getUnsafeBlocks((Entity)player, 2, false, face));
        }
        for (int i = 0; i < XuluRenderUtilBoss.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face).length; ++i) {
            Vec3d vector = XuluRenderUtilBoss.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face)[i];
            BlockPos targetPos = new BlockPos(player.getPositionVector()).add(vector.x, vector.y, vector.z);
            Block block = XuluRenderUtilBoss.mc.world.getBlockState(targetPos).getBlock();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInWater(Entity entity) {
        if (entity == null) {
            return false;
        }
        double y = entity.posY + 0.01;
        for (int x = MathHelper.floor((double)entity.posX); x < MathHelper.ceil((double)entity.posX); ++x) {
            for (int z = MathHelper.floor((double)entity.posZ); z < MathHelper.ceil((double)entity.posZ); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(XuluRenderUtilBoss.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isDrivenByPlayer(Entity entityIn) {
        return XuluRenderUtilBoss.mc.player != null && entityIn != null && entityIn.equals((Object)XuluRenderUtilBoss.mc.player.getRidingEntity());
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isAboveWater(Entity entity) {
        return XuluRenderUtilBoss.isAboveWater(entity, false);
    }

    public static boolean isAboveWater(Entity entity, boolean packet) {
        if (entity == null) {
            return false;
        }
        double y = entity.posY - (packet ? 0.03 : (XuluRenderUtilBoss.isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.floor((double)entity.posX); x < MathHelper.ceil((double)entity.posX); ++x) {
            for (int z = MathHelper.floor((double)entity.posZ); z < MathHelper.ceil((double)entity.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor((double)y), z);
                if (!(XuluRenderUtilBoss.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(XuluRenderUtilBoss.targets(player.getPositionVector(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace, face));
        } else {
            int extend = 1;
            for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) break;
                placeTargets.addAll(XuluRenderUtilBoss.targets(vec3d, !noScaffoldExtend, antiStep, legs, platform, antiDrop, raytrace, face));
                ++extend;
            }
        }
        ArrayList<Vec3d> removeList = new ArrayList<Vec3d>();
        for (Vec3d vec3d : placeTargets) {
            BlockPos pos = new BlockPos(vec3d);
            if (BlockUtil.isPositionPlaceable(pos, raytrace) != -1) continue;
            removeList.add(vec3d);
        }
        for (Vec3d vec3d : removeList) {
            placeTargets.remove((Object)vec3d);
        }
        return placeTargets;
    }

    public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean face) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList));
        } else {
            List<Vec3d> vec3ds = XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(vec3d, 2, false, face);
            if (vec3ds.size() == 4) {
                block5: for (Vec3d vector : vec3ds) {
                    BlockPos position = new BlockPos(vec3d).add(vector.x, vector.y, vector.z);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case 0: {
                            break block5;
                        }
                        case -1: 
                        case 1: 
                        case 2: {
                            continue block5;
                        }
                        case 3: {
                            placeTargets.add(vec3d.add(vector));
                        }
                        default: {
                            continue block5;
                        }
                    }
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
        }
        if (!face) {
            ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
            offsets.add(new Vec3d(1.0, 1.0, 0.0));
            offsets.add(new Vec3d(0.0, 1.0, -1.0));
            offsets.add(new Vec3d(0.0, 1.0, 1.0));
            Vec3d[] array = new Vec3d[offsets.size()];
            placeTargets.removeAll(Arrays.asList(BlockUtil.convertVec3ds(vec3d, offsets.toArray((T[])array))));
        }
        return placeTargets;
    }

    public static List<Vec3d> getOffsetList(int y, boolean floor, boolean face) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        if (face) {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
            offsets.add(new Vec3d(1.0, (double)y, 0.0));
            offsets.add(new Vec3d(0.0, (double)y, -1.0));
            offsets.add(new Vec3d(0.0, (double)y, 1.0));
        } else {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        }
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getOffsets(int y, boolean floor, boolean face) {
        List<Vec3d> offsets = XuluRenderUtilBoss.getOffsetList(y, floor, face);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        List<Vec3d> offsets = XuluRenderUtilBoss.getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop, face);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(XuluRenderUtilBoss.getOffsetList(1, false, face));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(2, false, face));
        }
        if (legs) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(0, false, face));
        }
        if (platform) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(-1, false, face));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getHeightOffsets(int min, int max) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        for (int i = min; i <= max; ++i) {
            offsets.add(new Vec3d(0.0, (double)i, 0.0));
        }
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return XuluRenderUtilBoss.isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }

    public static boolean isDead(Entity entity) {
        return !XuluRenderUtilBoss.isAlive(entity);
    }

    public static float getHealth(Entity entity) {
        if (XuluRenderUtilBoss.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static float getHealth(Entity entity, boolean absorption) {
        if (XuluRenderUtilBoss.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0f);
        }
        return 0.0f;
    }

    public static boolean canEntityFeetBeSeen(Entity entityIn) {
        return XuluRenderUtilBoss.mc.world.rayTraceBlocks(new Vec3d(XuluRenderUtilBoss.mc.player.posX, XuluRenderUtilBoss.mc.player.posX + (double)XuluRenderUtilBoss.mc.player.getEyeHeight(), XuluRenderUtilBoss.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }

    public static boolean isntValid(Entity entity, double range) {
        return entity == null || XuluRenderUtilBoss.isDead(entity) || entity.equals((Object)XuluRenderUtilBoss.mc.player) || entity instanceof EntityPlayer && OyVey.friendManager.isFriend(entity.getName()) || XuluRenderUtilBoss.mc.player.getDistanceSq(entity) > MathUtil.square(range);
    }

    public static boolean isValid(Entity entity, double range) {
        return !XuluRenderUtilBoss.isntValid(entity, range);
    }

    public static boolean holdingWeapon(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemSword || player.getHeldItemMainhand().getItem() instanceof ItemAxe;
    }

    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (XuluRenderUtilBoss.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById((int)1)))) {
            maxModifier *= 1.0 + 0.2 * (double)(Objects.requireNonNull(XuluRenderUtilBoss.mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById((int)1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }

    public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
        if (entity != null) {
            entity.motionX *= multiplier;
            entity.motionZ *= multiplier;
        }
    }

    public static boolean isEntityMoving(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return XuluRenderUtilBoss.mc.gameSettings.keyBindForward.isKeyDown() || XuluRenderUtilBoss.mc.gameSettings.keyBindBack.isKeyDown() || XuluRenderUtilBoss.mc.gameSettings.keyBindLeft.isKeyDown() || XuluRenderUtilBoss.mc.gameSettings.keyBindRight.isKeyDown();
        }
        return entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0;
    }

    public static boolean movementKey() {
        return XuluRenderUtilBoss.mc.player.movementInput.forwardKeyDown || XuluRenderUtilBoss.mc.player.movementInput.rightKeyDown || XuluRenderUtilBoss.mc.player.movementInput.leftKeyDown || XuluRenderUtilBoss.mc.player.movementInput.backKeyDown || XuluRenderUtilBoss.mc.player.movementInput.jump || XuluRenderUtilBoss.mc.player.movementInput.sneak;
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            double distTraveledLastTickX = entity.posX - entity.prevPosX;
            double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
            double speed = MathHelper.sqrt((double)(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ));
            return speed * 20.0;
        }
        return 0.0;
    }

    public static boolean holding32k(EntityPlayer player) {
        return XuluRenderUtilBoss.is32k(player.getHeldItemMainhand());
    }

    public static boolean is32k(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getTagCompound() == null) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        for (int i = 0; i < enchants.tagCount(); ++i) {
            NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") != 16) continue;
            int lvl = enchant.getInteger("lvl");
            if (lvl < 42) break;
            return true;
        }
        return false;
    }

    public static boolean simpleIs32k(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.SHARPNESS, (ItemStack)stack) >= 1000;
    }

    public static void moveEntityStrafe(double speed, Entity entity) {
        if (entity != null) {
            MovementInput movementInput = XuluRenderUtilBoss.mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = XuluRenderUtilBoss.mc.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                entity.motionX = 0.0;
                entity.motionZ = 0.0;
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
                entity.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                entity.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }

    public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
        return !shouldCheck || XuluRenderUtilBoss.mc.player.canEntityBeSeen(entity);
    }

    public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
        Color color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, (float)alpha / 255.0f);
        if (entity instanceof EntityPlayer) {
            if (colorFriends && OyVey.friendManager.isFriend((EntityPlayer)entity)) {
                color = new Color(0.33333334f, 1.0f, 1.0f, (float)alpha / 255.0f);
            }
            Killaura killaura = OyVey.moduleManager.getModuleByClass(Killaura.class);
            if (killaura.info.getValue().booleanValue() && Killaura.target != null && Killaura.target.equals((Object)entity)) {
                color = new Color(1.0f, 0.0f, 0.0f, (float)alpha / 255.0f);
            }
        }
        return color;
    }

    public static boolean isMoving() {
        return (double)XuluRenderUtilBoss.mc.player.moveForward != 0.0 || (double)XuluRenderUtilBoss.mc.player.moveStrafing != 0.0;
    }

    public static EntityPlayer getClosestEnemy(double distance) {
        EntityPlayer closest = null;
        for (EntityPlayer player : XuluRenderUtilBoss.mc.world.playerEntities) {
            if (XuluRenderUtilBoss.isntValid((Entity)player, distance)) continue;
            if (closest == null) {
                closest = player;
                continue;
            }
            if (!(XuluRenderUtilBoss.mc.player.getDistanceSq((Entity)player) < XuluRenderUtilBoss.mc.player.getDistanceSq((Entity)closest))) continue;
            closest = player;
        }
        return closest;
    }

    public static boolean checkCollide() {
        if (XuluRenderUtilBoss.mc.player.isSneaking()) {
            return false;
        }
        if (XuluRenderUtilBoss.mc.player.getRidingEntity() != null && XuluRenderUtilBoss.mc.player.getRidingEntity().fallDistance >= 3.0f) {
            return false;
        }
        return !(XuluRenderUtilBoss.mc.player.fallDistance >= 3.0f);
    }

    public static boolean isInLiquid() {
        if (XuluRenderUtilBoss.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        AxisAlignedBB bb = XuluRenderUtilBoss.mc.player.getRidingEntity() != null ? XuluRenderUtilBoss.mc.player.getRidingEntity().getEntityBoundingBox() : XuluRenderUtilBoss.mc.player.getEntityBoundingBox();
        int y = (int)bb.minY;
        for (int x = MathHelper.floor((double)bb.minX); x < MathHelper.floor((double)bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor((double)bb.minZ); z < MathHelper.floor((double)bb.maxZ) + 1; ++z) {
                Block block = XuluRenderUtilBoss.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block instanceof BlockAir) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static boolean isOnLiquid(double offset) {
        if (XuluRenderUtilBoss.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        AxisAlignedBB bb = XuluRenderUtilBoss.mc.player.getRidingEntity() != null ? XuluRenderUtilBoss.mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -offset, 0.0) : XuluRenderUtilBoss.mc.player.getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -offset, 0.0);
        boolean onLiquid = false;
        int y = (int)bb.minY;
        for (int x = MathHelper.floor((double)bb.minX); x < MathHelper.floor((double)(bb.maxX + 1.0)); ++x) {
            for (int z = MathHelper.floor((double)bb.minZ); z < MathHelper.floor((double)(bb.maxZ + 1.0)); ++z) {
                Block block = XuluRenderUtilBoss.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == Blocks.AIR) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isAboveLiquid(Entity entity) {
        if (entity == null) {
            return false;
        }
        double n = entity.posY + 0.01;
        for (int i = MathHelper.floor((double)entity.posX); i < MathHelper.ceil((double)entity.posX); ++i) {
            for (int j = MathHelper.floor((double)entity.posZ); j < MathHelper.ceil((double)entity.posZ); ++j) {
                if (!(XuluRenderUtilBoss.mc.world.getBlockState(new BlockPos(i, (int)n, j)).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos(XuluRenderUtilBoss.mc.player.getRidingEntity() != null ? XuluRenderUtilBoss.mc.player.getRidingEntity().posX : XuluRenderUtilBoss.mc.player.posX, XuluRenderUtilBoss.mc.player.getRidingEntity() != null ? XuluRenderUtilBoss.mc.player.getRidingEntity().posY : XuluRenderUtilBoss.mc.player.posY, XuluRenderUtilBoss.mc.player.getRidingEntity() != null ? XuluRenderUtilBoss.mc.player.getRidingEntity().posZ : XuluRenderUtilBoss.mc.player.posZ);
    }

    public static boolean checkForLiquid(Entity entity, boolean b) {
        if (entity == null) {
            return false;
        }
        double posY = entity.posY;
        double n = b ? 0.03 : (entity instanceof EntityPlayer ? 0.2 : 0.5);
        double n2 = posY - n;
        for (int i = MathHelper.floor((double)entity.posX); i < MathHelper.ceil((double)entity.posX); ++i) {
            for (int j = MathHelper.floor((double)entity.posZ); j < MathHelper.ceil((double)entity.posZ); ++j) {
                if (!(XuluRenderUtilBoss.mc.world.getBlockState(new BlockPos(i, MathHelper.floor((double)n2), j)).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isOnLiquid() {
        double y = XuluRenderUtilBoss.mc.player.posY - 0.03;
        for (int x = MathHelper.floor((double)XuluRenderUtilBoss.mc.player.posX); x < MathHelper.ceil((double)XuluRenderUtilBoss.mc.player.posX); ++x) {
            for (int z = MathHelper.floor((double)XuluRenderUtilBoss.mc.player.posZ); z < MathHelper.ceil((double)XuluRenderUtilBoss.mc.player.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor((double)y), z);
                if (!(XuluRenderUtilBoss.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static double[] forward(double speed) {
        float forward = XuluRenderUtilBoss.mc.player.movementInput.moveForward;
        float side = XuluRenderUtilBoss.mc.player.movementInput.moveStrafe;
        float yaw = XuluRenderUtilBoss.mc.player.prevRotationYaw + (XuluRenderUtilBoss.mc.player.rotationYaw - XuluRenderUtilBoss.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
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

    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder distanceSB = new StringBuilder();
        for (EntityPlayer player : XuluRenderUtilBoss.mc.world.playerEntities) {
            int hpRaw = (int)XuluRenderUtilBoss.getHealth((Entity)player);
            String hp = dfHealth.format(hpRaw);
            healthSB.append("\u00a7");
            if (hpRaw >= 20) {
                healthSB.append("a");
            } else if (hpRaw >= 10) {
                healthSB.append("e");
            } else if (hpRaw >= 5) {
                healthSB.append("6");
            } else {
                healthSB.append("c");
            }
            healthSB.append(hp);
            int distanceInt = (int)XuluRenderUtilBoss.mc.player.getDistance((Entity)player);
            String distance = dfDistance.format(distanceInt);
            distanceSB.append("\u00a7");
            if (distanceInt >= 25) {
                distanceSB.append("a");
            } else if (distanceInt > 10) {
                distanceSB.append("6");
            } else if (distanceInt >= 50) {
                distanceSB.append("7");
            } else {
                distanceSB.append("c");
            }
            distanceSB.append(distance);
            output.put(healthSB.toString() + " " + (OyVey.friendManager.isFriend(player) ? "\u00a7b" : "\u00a7r") + player.getName() + " " + distanceSB.toString() + " \u00a7f" + OyVey.totemPopManager.getTotemPopString(player) + OyVey.potionManager.getTextRadarPotion(player), (int)XuluRenderUtilBoss.mc.player.getDistance((Entity)player));
            healthSB.setLength(0);
            distanceSB.setLength(0);
        }
        if (!output.isEmpty()) {
            output = MathUtil.sortByValue(output, false);
        }
        return output;
    }

    public static void swingArmNoPacket(EnumHand hand, EntityLivingBase entity) {
        ItemStack stack = entity.getHeldItem(hand);
        if (!stack.isEmpty() && stack.getItem().onEntitySwing(entity, stack)) {
            return;
        }
        if (!entity.isSwingInProgress || entity.swingProgressInt >= ((XuluLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.swingProgressInt < 0) {
            entity.swingProgressInt = -1;
            entity.isSwingInProgress = true;
            entity.swingingHand = hand;
        }
    }

    public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
        return entity.posY >= (double)blockPos.getY();
    }
}

